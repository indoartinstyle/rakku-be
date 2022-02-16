package in.as.sixtynine.rakku.mappers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.as.sixtynine.rakku.dtos.OrderRequestDto;
import in.as.sixtynine.rakku.entities.OrderEntity;
import org.springframework.stereotype.Service;

/**
 * @Author Sanjay Das (s0d062y), Created on 02/01/22
 */


@Service
public class OrderMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderEntity toEntity(OrderRequestDto requestDto) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(requestDto, OrderEntity.class);
    }

}
