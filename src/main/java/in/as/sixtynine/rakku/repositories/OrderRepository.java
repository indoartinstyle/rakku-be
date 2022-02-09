package in.as.sixtynine.rakku.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import in.as.sixtynine.rakku.entities.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends CosmosRepository<OrderEntity, String> {
    @Query("SELECT * FROM c where c.type='OrderEntity' AND IS_NULL(c.itemCourierTrackID)")
    List<OrderEntity> getAllNonDispatchedOrder();
}
