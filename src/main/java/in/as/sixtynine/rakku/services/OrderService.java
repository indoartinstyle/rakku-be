package in.as.sixtynine.rakku.services;

import com.azure.cosmos.implementation.guava25.collect.Sets;
import com.azure.cosmos.models.SqlQuerySpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import in.as.sixtynine.rakku.dtos.*;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.mappers.OrderMapper;
import in.as.sixtynine.rakku.repositories.OrderRepository;
import in.as.sixtynine.rakku.repositories.ProductRepository;
import in.as.sixtynine.rakku.userservice.entity.Address;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.repository.UserRepository;
import in.as.sixtynine.rakku.userservice.utils.ERole;
import in.as.sixtynine.rakku.userservice.utils.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final UserRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${delivery.from.address}")
    private String fromAddr;

    @Value("${delivery.from.name}")
    private String fromName;

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
            deliveryDetailsDto.setFromName(fromName);
            deliveryDetailsDto.setFromNumber(phoneno);
            deliveryDetailsDto.setItems(order.getItems());
            deliveryDetailsDto.setItemTotalCost(order.getItemTotalCost());
            return deliveryDetailsDto;
        }).collect(Collectors.toList());
    }


    public void test(List<OrderEntity> saleInfo) {
        log.info("Registering users....");
        AtomicInteger at = new AtomicInteger(1);

        saleInfo.forEach(order -> {
            log.info("Counting {}...", at.getAndIncrement());
            try {
                final String customerName = order.getCustomerName();
                final long customerNumber = order.getCustomerNumber();
                final String customerAddress = order.getCustomerAddress();
                final String[] s = customerName.split(" ");
                User user = new User();
                if (s.length > 1) {
                    user.setFirstName(s[0]);
                    user.setLastName("");
                    for (int i = 1; i < s.length; i++) {
                        user.setLastName(user.getLastName() + " " + s[i]);
                    }
                }
                if ((customerNumber + "").length() == 12) {
                    user.setPhoneNumber(customerNumber);
                }
                if ((customerNumber + "").length() == 10) {
                    user.setPhoneNumber(Long.parseLong("91" + customerNumber));
                }
                Map<String, Address> addr = new HashMap<>();
                Address address = new Address();
                address.setLine1(customerAddress);
                addr.put("PRIMARY", address);
                user.setAddresses(addr);
                user.setUserType(UserType.BUYER.name());
                user.setRoles(Sets.newHashSet(ERole.USER.getRoleName()));
                user.setCreatedTime(System.currentTimeMillis());
                user.setLastUpdatedTime(System.currentTimeMillis());
                user.setId(generateUserID(user));
                final User save = repository.save(user);
                log.info("New Users = {}", save);
            } catch (Exception e) {
                log.error("Order - > {}\nError=> {}", order, e.getMessage());
            }
        });
        log.info("Registering users done....");
    }

    private String generateUserID(User user) {
        return user.getFirstName().toLowerCase() + "." + user.getLastName().toLowerCase() + "." + user.getPhoneNumber();
    }

    public SalesDto getAllSales(String year, String month, String day, boolean isItemReq) throws ParseException {
        String myDate = year + "/" + month + "/" + day + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(myDate);
        long millis = date.getTime();
        final List<OrderEntity> saleInfo = orderRepository.getSaleInfo(millis);
        test(saleInfo);
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
}
