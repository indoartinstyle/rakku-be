package in.as.sixtynine.rakku.services;

import com.azure.cosmos.implementation.guava25.collect.Sets;
import in.as.sixtynine.rakku.repositories.OrderRepository;
import in.as.sixtynine.rakku.userservice.entity.Address;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.repository.UserRepository;
import in.as.sixtynine.rakku.userservice.utils.ERole;
import in.as.sixtynine.rakku.userservice.utils.UserType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Sanjay Das (s0d062y), Created on 30/03/22
 */

@Log4j2
@Service
public class ScriptRunner {

    @Autowired
    UserRepository repository;


    @Autowired
    OrderRepository orderRepository;

    @PostConstruct
    public void init() {
        log.info("Registering users....");
        AtomicInteger at = new AtomicInteger(1);
        orderRepository.getSaleInfo(0L).forEach(order -> {
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
                repository.save(user);
            } catch (Exception e) {
                log.error("Order - > {}\nError=> {}", order, e.getMessage());
            }
        });
        log.info("Registering users done....");
    }

    private String generateUserID(User user) {
        return user.getFirstName().toLowerCase() + "." + user.getLastName().toLowerCase() + "." + user.getPhoneNumber();
    }
}
