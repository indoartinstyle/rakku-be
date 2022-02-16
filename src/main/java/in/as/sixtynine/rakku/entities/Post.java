package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.util.List;

import static in.as.sixtynine.rakku.constants.DBConstants.RBOX;


@Data
@NoArgsConstructor
@Container(containerName = RBOX)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    @Id
    private String id;

    @PartitionKey
    private final String type = Post.class.getSimpleName();

    private String region;
    private String org;
    private String username;
    private String docType;
    private List<String> tags;
    private String content;
    private long createdTime;
    @Version
    @JsonProperty("_etag")
    private String _etag;
    @JsonProperty("_self")
    private String self;
}
