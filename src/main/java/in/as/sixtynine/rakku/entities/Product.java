package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> imgUrl =new ArrayList<>();
    @NotEmpty
    private String itemModelName;
    @NotEmpty
    private String itemColor;
    @NotEmpty
    private String itemSize;
    private double cost;
    @Min(1)
    private int stock;

    private long createdTime;
    private String createdBy;
    private String updatedBy;

    @Version
    @JsonProperty("_etag")
    private String _etag;
    @JsonProperty("_self")
    private String self;
}
