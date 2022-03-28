package in.as.sixtynine.rakku.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import in.as.sixtynine.rakku.entities.Product;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends CosmosRepository<Product, String> {

    @Query("SELECT c.id from c WHERE c.type='Product' ORDER BY c.createdTime DESC OFFSET 0 LIMIT 1")
    List<Product> getCount();

    default int getSeqNum() {
        final List<Product> products = getCount();
        if (products == null || products.isEmpty()) {
            return 1;
        }
        final String num = products.get(0).getId().split("-")[1];
        return Integer.parseInt(num) + 1;
    }

    @Query("SELECT * from c WHERE c.stock > 0 ORDER BY c.itemModelName ASC")
    List<Product> getAllAvailableProducts();

    @Query("SELECT * from c WHERE c.type='Product'")
    List<Product> getAllProduct();

}
