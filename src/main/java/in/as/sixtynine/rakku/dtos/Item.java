package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @Author Sanjay Das (s0d062y), Created on 09/02/22
 */

@Data
public class Item {
    @NotBlank
    private String itemModelName;
    @NotBlank
    private String itemID;
    @NotBlank
    private String itemColor;
    @NotBlank
    private String itemSize;
    @Min(1)
    private double itemMRP;
    @Min(1)
    private int itemQty;
}
