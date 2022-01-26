package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

import static in.as.sixtynine.rakku.constants.DBConstants.POSTS;


@Data
@NoArgsConstructor
@Container(containerName = POSTS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    @Id
    private String id;
    @PartitionKey
    private String region;
    private String org;
    private String username;
    private String docType;
    private List<String> tags;
    private String content;
    private long createdTime;
    @JsonProperty("_etag")
    private String etag;
    @JsonProperty("_self")
    private String self;
}
