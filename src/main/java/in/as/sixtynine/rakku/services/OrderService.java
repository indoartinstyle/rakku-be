package in.as.sixtynine.rakku.services;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import in.as.sixtynine.rakku.dtos.*;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.mappers.OrderMapper;
import in.as.sixtynine.rakku.repositories.OrderRepository;
import in.as.sixtynine.rakku.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    @Value("${delivery.from.address}")
    private String fromAddr;

    @Value("${delivery.from.phoneno}")
    private Long phoneno;

    public OrderEntity createOrder(OrderRequestDto requestDto, String takenBy) {
        final OrderEntity orderEntity = mapper.toEntity(requestDto);
        orderEntity.setCreatedTime(System.currentTimeMillis());
        orderEntity.setId(UUID.randomUUID().toString());
        orderEntity.setOrderTakenBy(takenBy);
        fillTotalCost(orderEntity);
        final Collection<Product> products = validateStock(orderEntity);
        final OrderEntity save = orderRepository.save(orderEntity);
        productRepository.saveAll(products);
        return save;
    }

    private Collection<Product> validateStock(OrderEntity orderEntity) {
        final Map<String, Item> items = new HashMap<>();
        orderEntity.getItems().forEach(item -> {
            items.put(item.getItemID(), item);
        });
        final Map<String, Product> products = new HashMap<>();
        productRepository.findAllById(items.keySet()).forEach(product -> {
            products.put(product.getId(), product);
        });
        if (products.size() != items.size()) {
            throw new RuntimeException("All items are not present, available products are " + products.keySet());
        }
        List<String> lstInsufficientStock = new ArrayList<>();
        products.forEach((pid, product) -> {
            final Item item = items.get(pid);
            final int itemQty = item.getItemQty();
            final int stock = product.getStock();
            if (stock >= itemQty) {
                product.setStock(stock - itemQty);
            } else {
                lstInsufficientStock.add("Item " + item.getItemModelName() + " , required qty is " + itemQty + ", but stock is " + stock);
            }
        });
        if (!lstInsufficientStock.isEmpty()) {
            throw new RuntimeException(lstInsufficientStock.toString());
        }
        return products.values();
    }

    private void fillTotalCost(OrderEntity orderEntity) {
        AtomicDouble total = new AtomicDouble(0);
        orderEntity.getItems().forEach(item -> {
            final double itemMRP = item.getItemMRP();
            final int itemQty = item.getItemQty();
            total.addAndGet((itemMRP * itemQty));
        });
        orderEntity.setItemTotalCost(total.get());
    }

    public List<OrderEntity> update(OrderDispatchBulkDto orderRequestDto, String name) {
        final Iterable<OrderEntity> allById = orderRepository.findAllById(orderRequestDto.getOrders().stream().map(OrderDispatchDto::getId).collect(Collectors.toList()));
        Map<String, OrderEntity> allOrders = new HashMap<>();
        allById.forEach(order -> allOrders.put(order.getId(), order));
        if (allOrders.size() != orderRequestDto.getOrders().size()) {
            throw new RuntimeException("some orders not found");
        }
        List<OrderEntity> toBeUpdated = new ArrayList<>();
        orderRequestDto.getOrders().forEach(updateablOrder -> {
            final OrderEntity orderEntity = allOrders.get(updateablOrder.getId());
            orderEntity.setItemCourierPartner(updateablOrder.getItemCourierPartner());
            orderEntity.setItemCourierTrackID(updateablOrder.getItemCourierTrackID());
            orderEntity.setItemCourierStatus("DISPATCHED");
            orderEntity.setOrderDispatchBy(name);
            toBeUpdated.add(orderEntity);
        });
        return Lists.newArrayList(orderRepository.saveAll(toBeUpdated));
    }

    public List<DeliveryDetailsDto> getAllNonDispatchedOrder() {
        final List<OrderEntity> allNonDispatchedOrder = orderRepository.getAllNonDispatchedOrder();
        return allNonDispatchedOrder.stream().map(order -> {
            final String customerName = order.getCustomerName();
            final long customerNumber = order.getCustomerNumber();
            final String customerAddress = order.getCustomerAddress();
            final DeliveryDetailsDto deliveryDetailsDto = new DeliveryDetailsDto();
            deliveryDetailsDto.setOrderID(order.getId());
            deliveryDetailsDto.setCustomerAddress(customerAddress);
            deliveryDetailsDto.setCustomerName(customerName);
            deliveryDetailsDto.setCustomerNumber(customerNumber);
            deliveryDetailsDto.setFromAddress(fromAddr);
            deliveryDetailsDto.setFromNumber(phoneno);
            deliveryDetailsDto.setItems(order.getItems());
            deliveryDetailsDto.setItemTotalCost(order.getItemTotalCost());
            return deliveryDetailsDto;
        }).collect(Collectors.toList());
    }
}
