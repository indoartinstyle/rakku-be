package in.as.sixtynine.rakku.dtos;

import lombok.Data;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/02/22
 */

@Data
public class OrderDTo {
    private String customerName;
    private long customerNumber;
    private String customerAddress;
    private String customerFromSocial;

    private String itemModelName;
    private String itemID;
    private String itemColor;
    private String itemSize;
    private double itemMRP;
    private int itemQty;
}
