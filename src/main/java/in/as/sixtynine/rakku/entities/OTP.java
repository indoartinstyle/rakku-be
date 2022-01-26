package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@Container(containerName = "OTP")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OTP {
    @Id
    @PartitionKey
    private String mobileNo;
    private String otp;
    private long ttl;
}
