package in.as.sixtynine.rakku.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {
    private String region;
    private String username;
    private String org;
    private String docType;
    private List<String> tags;
    private long createdTime;
    private String content;
    @JsonProperty("_etag")
    private String etag;
}
