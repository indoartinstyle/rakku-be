package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/02/22
 */

@Data
public class OrderDispatchBulkDto {
    @NotEmpty
    private List<OrderDispatchDto> orders;
}
