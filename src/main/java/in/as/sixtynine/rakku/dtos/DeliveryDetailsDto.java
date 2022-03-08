package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import java.util.List;

/**
 * @Author Sanjay Das (s0d062y), Created on 09/02/22
 */

@Data
public class DeliveryDetailsDto {
    private String orderID;

    private long customerNumber;
    private String customerAddress;
    private String customerName;

    private String fromName;
    private long fromNumber;
    private String fromAddress;

    private List<Item> items;
    private double itemTotalCost;

}
