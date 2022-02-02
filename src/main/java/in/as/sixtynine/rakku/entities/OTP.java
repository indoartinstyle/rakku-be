package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import static in.as.sixtynine.rakku.constants.DBConstants.RBOX;


@Data
@NoArgsConstructor
@Container(containerName = RBOX)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OTP {
    @Id
    private String id;
    @PartitionKey
    private final String type = OTP.class.getSimpleName();

    private String mobileNo;
    private String otp;
    private long ttl;
}
