package in.as.sixtynine.rakku.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.as.sixtynine.rakku.dtos.Item;
import in.as.sixtynine.rakku.dtos.ReturnData;
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
public class OrderEntity {
    @Id
    private String id;

    @PartitionKey
    private final String type = OrderEntity.class.getSimpleName();

    private String customerName;
    private long customerNumber;
    private String customerAddress;
    private String customerFromSocial;
    private List<Item> items;
    private List<ReturnData> returns;
    private double itemTotalGST;
    private double itemTotalCost;

    private String oldItemCourierPartner;
    private String oldItemCourierTrackID;

    private String itemCourierPartner;
    private String itemCourierTrackID;
    private String itemCourierStatus;

    private String orderTakenBy;
    private String orderDispatchBy;

    private long createdTime;

    @Version
    @JsonProperty("_etag")
    private String _etag;
    @JsonProperty("_self")
    private String self;
}
