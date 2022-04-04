package in.as.sixtynine.rakku.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import in.as.sixtynine.rakku.dtos.*;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.mappers.OrderMapper;
import in.as.sixtynine.rakku.repositories.OrderRepository;
import in.as.sixtynine.rakku.repositories.ProductRepository;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.utils.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;
    private final InterceptingService interceptingService;
    private final ReturnService returnService;
    private final OrderProductTransaction orderProductTransaction;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${delivery.from.address}")
    private String fromAddr;

    @Value("${delivery.from.name}")
    private String fromName;

    @Value("${delivery.from.phoneno}")
    private Long phoneno;

    public OrderEntity createOrder(OrderRequestDto requestDto, String takenBy) {
        log.info("Creating Order - in {}", Thread.currentThread().getName());
        interceptingService.createUserFromOrder(requestDto);
        final OrderEntity orderEntity = mapper.toEntity(requestDto);
        orderEntity.setCreatedTime(System.currentTimeMillis());
        orderEntity.setId(UUID.randomUUID().toString());
        orderEntity.setOrderTakenBy(takenBy);
        final OrderEntity save = doCreateOrder(orderEntity);
        return save;
    }

    private OrderEntity doCreateOrder(OrderEntity orderEntity) {
        fillTotalCost(orderEntity);
        final Collection<Product> products = validateStock(orderEntity.getItems());
        orderEntity.setStatus("PENDING");
        OrderEntity save = orderRepository.save(orderEntity);
        try {
            save.setStatus("CONFIRM");
            save = orderProductTransaction.updateOrderProduct(save, new ArrayList<>(products));
            return save;
        } catch (JsonProcessingException e) {
            log.error("Error = {} ", e.getMessage());
            throw new RuntimeException("" + e.getMessage());
        }

    }

    private Collection<Product> validateStock(List<Item> acquiredItems) {
        final Map<String, Item> items = new HashMap<>();
        acquiredItems.forEach(item -> {
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
            interceptingService.sendDispatchNotification(orderEntity);
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
            deliveryDetailsDto.setFromName(fromName);
            deliveryDetailsDto.setFromNumber(phoneno);
            deliveryDetailsDto.setItems(order.getItems());
            deliveryDetailsDto.setItemTotalCost(order.getItemTotalCost());
            return deliveryDetailsDto;
        }).collect(Collectors.toList());
    }

    public SalesDto getAllSales(String year, String month, String day, boolean isItemReq) throws ParseException {
        String myDate = year + "/" + month + "/" + day + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(myDate);
        long millis = date.getTime();
        final List<OrderEntity> saleInfo = orderRepository.getSaleInfo(millis);
        final SalesDto res = new SalesDto();
        saleInfo.forEach(order -> {
            res.setTotalRevenue(res.getTotalRevenue() + order.getItemTotalCost());
            order.getItems().forEach(item -> {
                res.setNoOfItem(res.getNoOfItem() + 1);
            });
        });
        if (isItemReq) {
            List<Map> allItems = new ArrayList<>();
            res.setAllItems(allItems);

            Calendar calendar = Calendar.getInstance();
            Map<String, Integer> prdQtyMap = new HashMap<>();
            saleInfo.forEach(orderEntity -> {
                final long createdTime = orderEntity.getCreatedTime();
                final String orderID = orderEntity.getId();
                orderEntity.getItems().forEach(item -> {
                    Integer totQty = prdQtyMap.get(item.getItemID());
                    totQty = totQty == null ? item.getItemQty() : (totQty + item.getItemQty());
                    prdQtyMap.put(item.getItemID(), totQty);
                    final Map map = objectMapper.convertValue(item, Map.class);
                    map.put("orderID", orderID);
                    calendar.setTimeInMillis(createdTime);
                    map.put("date", calendar.getTime());
                    map.put("courierPartner", orderEntity.getItemCourierPartner());
                    map.put("trackingNo", orderEntity.getItemCourierTrackID());
                    allItems.add(map);
                });
            });


            final Iterable<Product> allById = productRepository.findAllById(prdQtyMap.keySet());
            allById.forEach(product -> {
                final double cost = product.getCost();
                final Integer qty = prdQtyMap.get(product.getId());
                final double total = cost * qty;
                res.setTotalCost(res.getTotalCost() + total);
            });
        }
        return res;
    }

    @Transactional
    public OrderEntity returnProducts(User loggedUser, String orderid, ReturnData returnDto) throws JsonProcessingException {
        final Optional<OrderEntity> byId = orderRepository.findById(orderid);
        if (byId.isEmpty()) {
            throw new RuntimeException("No order found for ID...");
        }
        if (isOrderBelongsToLoggedUsers(loggedUser, byId.get()) || isAdmin(loggedUser)) {
            final OrderEntity orderEntity = returnService.performReturnFlow(byId.get(), returnDto);
            fillTotalCost(orderEntity);
            orderEntity.setOldItemCourierPartner(orderEntity.getItemCourierPartner());
            orderEntity.setOldItemCourierTrackID(orderEntity.getItemCourierTrackID());
            orderEntity.setItemCourierTrackID(null);
            orderEntity.setItemCourierPartner(null);

            if (!CollectionUtils.isEmpty(returnDto.getOnExchange())) {
                final OrderEntity orderEntity1 = returnService.returnProductTransactionOperation(orderEntity, new ArrayList<>(validateStock(returnDto.getOnExchange())));
                log.info("Order return successful... {}", orderEntity1);
                return orderEntity1;
            }
            orderRepository.save(orderEntity);
            return orderEntity;
        } else {
            throw new RuntimeException("Permission denied ...");
        }
    }

    private boolean isAdmin(User loggedUser) {
        return loggedUser.getRoles().contains(ERole.ADMIN.getRoleName());
    }

    private boolean isOrderBelongsToLoggedUsers(User loggedUser, OrderEntity orderEntity) {
        final String customerNumber = new StringBuilder("" + orderEntity.getCustomerNumber()).reverse().toString();
        final String loggedUserNumber = new StringBuilder("" + loggedUser.getPhoneNumber()).reverse().toString();
        return customerNumber.substring(0, 10).equalsIgnoreCase(loggedUserNumber.substring(0, 10));
    }
}
