package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String PREFIX = "PRODUCT-";

    private final ProductRepository productRepository;

    public Product createNewItem(Product product, String name) {
        if (product.getCost() <= 0D) {
            throw new RuntimeException("Cost cannot be < 0");
        }
        product.setId(PREFIX + productRepository.getSeqNum());
        product.setItemModelName(product.getItemModelName() + "-" + product.getItemSize() + "-" + product.getItemColor());
        product.setCreatedTime(System.currentTimeMillis());
        product.setCreatedBy(name);
        return productRepository.save(product);
    }

    public Product updateStock(Product product, String name) {
        if (product.getStock() <= 0) {
            throw new RuntimeException("Cost cannot be < 0");
        }
        final Optional<Product> byId = productRepository.findById(product.getId());
        final Product toBeUpdated = byId.get();
        final int currentStock = toBeUpdated.getStock() < 0 ? 0 : toBeUpdated.getStock();
        toBeUpdated.setStock(currentStock + product.getStock());
        toBeUpdated.setUpdatedBy(name);
        return productRepository.save(toBeUpdated);
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllAvailableProducts();
    }

}
