package in.as.sixtynine.rakku.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import in.as.sixtynine.rakku.entities.OTP;
import org.springframework.stereotype.Repository;


@Repository
public interface OTPRepository extends CosmosRepository<OTP, String> {
}
