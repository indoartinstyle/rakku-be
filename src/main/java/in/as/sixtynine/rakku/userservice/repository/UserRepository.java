package in.as.sixtynine.rakku.userservice.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import in.as.sixtynine.rakku.userservice.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CosmosRepository<User, String> {
    List<User> findByPhoneNumber(long phoneNumber);
}
