package in.as.sixtynine.rakku.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/02/22
 */

@Data
public class OrderDispatchDto {
    @NotBlank
    private String id;
    @NotBlank
    private String itemCourierPartner;
    @NotBlank
    private String itemCourierTrackID;
}
