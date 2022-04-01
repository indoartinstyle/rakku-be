package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.dtos.Item;
import in.as.sixtynine.rakku.dtos.ReturnData;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.enums.EReturnReason;
import in.as.sixtynine.rakku.enums.EReturnResolveType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

/**
 * @Author Sanjay Das (s0d062y), Created on 01/04/22
 */

@Log4j2
@Service
public class ReturnService {

    public OrderEntity performReturnFlow(OrderEntity orderEntity, ReturnData returnDto) {
        log.info("executing return flow for order = {}, with return details={}", orderEntity.getId(), returnDto);
        validate(orderEntity, returnDto);
        final Optional<Item> itemTobeReturned = orderEntity.getItems().stream().filter(item -> item.getItemID().equalsIgnoreCase(returnDto.getReturnedItemID())).findFirst();
        final Item item = itemTobeReturned.get();
        orderEntity.getItems().remove(item);
        if (!CollectionUtils.isEmpty(returnDto.getOnExchange())) {
            returnDto.getOnExchange().forEach(orderEntity.getItems()::add);
        }
        return orderEntity;
    }

    private void validate(OrderEntity orderEntity, ReturnData returnDto) {
        final boolean isItemUnderOrder = orderEntity.getItems().stream().anyMatch(item -> item.getItemID().equalsIgnoreCase(returnDto.getReturnedItemID()));
        // 1. Check return item belongs to order
        if (!isItemUnderOrder) {
            throw new RuntimeException("This Item is not belongs to this order....");
        }

        returnDto.setReturnReason(EReturnReason.valueOf(returnDto.getReturnReason().toUpperCase()).name());
        returnDto.setResolveType(EReturnResolveType.valueOf(returnDto.getResolveType().toUpperCase()).name());

        if (returnDto.getResolveType().equalsIgnoreCase(EReturnResolveType.MONEY_RETURN.name())) {
            if (!returnDto.getOnExchange().isEmpty()) {
                throw new RuntimeException("If resolve type is money return then exchange list is not required...");
            }
        }
        if (returnDto.getResolveType().equalsIgnoreCase(EReturnResolveType.DIFFERENT_EXCHANGE.name())) {
            if (returnDto.getOnExchange().isEmpty()) {
                throw new RuntimeException("If resolve type is DIFFERENT_EXCHANGE then exchange list is required...");
            }
        }
    }
}
