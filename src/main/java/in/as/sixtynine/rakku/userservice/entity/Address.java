package in.as.sixtynine.rakku.userservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author Sanjay Das (s0d062y), Created on 29/03/22
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    private String line1;
    private String line2;
    private String cityVillage;
    private String district;
    private String state;
    private Integer pin;
}
