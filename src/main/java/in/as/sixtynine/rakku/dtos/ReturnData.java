package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author Sanjay Das (s0d062y), Created on 01/04/22
 */

@Data
public class ReturnData {
    @NotBlank
    private String returnedItemID;
    @NotBlank
    private String resolveType;
    @NotBlank
    private String returnReason;

    private String returnReasonDesc;

    private long returnDate;

    private List<Item> onExchange;

    @Min(1)
    private int itemQty;
}
