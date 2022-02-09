package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import static in.as.sixtynine.rakku.constants.DBConstants.RBOX;


@Data
@NoArgsConstructor
@Container(containerName = RBOX)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    private String id;

    @PartitionKey
    private final String type = Product.class.getSimpleName();

    private String itemModelName;
    private String itemColor;
    private String itemSize;
    private double cost;

    private int stock;

    private long createdTime;
    private String createdBy;

    @JsonProperty("_etag")
    private String etag;
    @JsonProperty("_self")
    private String self;
}
