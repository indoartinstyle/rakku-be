package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/03/22
 */
@Data
public class SalesDto {
    private double totalRevenue;
    private int noOfItem;
    private List<Map> allItems;
}
