package in.as.sixtynine.rakku.userservice.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Set;


@Data
@NoArgsConstructor
@Container(containerName = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    private String id;
    private String lastName;
    private String avatarUrl;
    private String firstName;
    private String email;
    private long phoneNumber;
    @PartitionKey
    private String userType;
    private String level;
    private Set<String> roles;
    private Set<String> following;
    private long lastLoginTime;
    private long createdTime;
    private long lastUpdatedTime;
}
