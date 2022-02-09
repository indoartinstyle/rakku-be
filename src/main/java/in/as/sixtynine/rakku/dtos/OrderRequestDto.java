package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/02/22
 */

@Data
public class OrderRequestDto {
    @NotBlank
    private String customerName;
    private long customerNumber;
    @NotBlank
    private String customerAddress;
    @NotBlank
    private String customerFromSocial;
    @NotEmpty
    private List<Item> items;
}
