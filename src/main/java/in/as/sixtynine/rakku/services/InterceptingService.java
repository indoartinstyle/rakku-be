package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.dtos.OrderRequestDto;
import in.as.sixtynine.rakku.entities.OTP;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.repositories.OTPRepository;
import in.as.sixtynine.rakku.userservice.entity.Address;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.service.UserManagementService;
import in.as.sixtynine.rakku.userservice.utils.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author Sanjay Das (s0d062y), Created on 30/03/22
 */

@Service
@Log4j2
@RequiredArgsConstructor
@Async("interceptThread")
public class InterceptingService {

    private final UserManagementService userManagementService;
    private final OTPRepository otpRepository;
    private final MessageSenderService messageSenderService;

    @Getter
    @Value("${MSG_TEMPLATE}")
    private String msgTemplate;

    @Getter
    @Value("${delivery.from.name}")
    private String orgName;

    @Async("interceptThread")
    public void createUserFromOrder(OrderRequestDto requestDto) {
        log.info("Running Interceptor createUserFromOrder() - in {}", Thread.currentThread().getName());
        try {
            if (("" + requestDto.getCustomerNumber()).length() == 10 || ("" + requestDto.getCustomerNumber()).length() == 12) {
                log.info("Number is correct...");
            } else {
                throw new RuntimeException("Number is not correct");
            }
            if (("" + requestDto.getCustomerNumber()).length() == 10) {
                requestDto.setCustomerNumber(Long.parseLong("91" + requestDto.getCustomerNumber()));
            }
            if (isUserAlreadyRegistered(requestDto)) {
                log.info("User is registered already...");
                return;
            }
            String otpStr = UUID.randomUUID().toString();
            craeteLocalOTP(requestDto, otpStr);
            User user = extraxtUser(requestDto);
            log.info("New Users = {}, Registering this user... ", userManagementService.saveUser(user, otpStr));
        } catch (Exception e) {
            log.info("Failed to create user from OrderRequest");
        }

    }

    private boolean isUserAlreadyRegistered(OrderRequestDto requestDto) {
        try {
            if (Objects.nonNull(userManagementService.getUserByMobileNumber("" + requestDto.getCustomerNumber()))) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private User extraxtUser(OrderRequestDto requestDto) {
        final String customerName = requestDto.getCustomerName();
        final String customerAddress = requestDto.getCustomerAddress();
        final String[] s = customerName.split(" ");
        User user = new User();
        if (s.length > 1) {
            user.setFirstName(s[0]);
            user.setLastName("");
            for (int i = 1; i < s.length; i++) {
                user.setLastName(user.getLastName() + " " + s[i]);
            }
        } else {
            user.setFirstName(s[0]);
            user.setLastName("");
        }
        user.setLastName(user.getLastName().replaceAll(" ", ""));
        user.setPhoneNumber(requestDto.getCustomerNumber());
        Map<String, Address> addr = new HashMap<>();
        Address address = new Address();
        address.setLine1(customerAddress);
        addr.put("PRIMARY", address);
        user.setAddresses(addr);
        user.setUserType(UserType.BUYER.name());
        return user;
    }

    private void craeteLocalOTP(OrderRequestDto requestDto, String otpStr) {
        OTP otp = new OTP();
        otp.setOtp(otpStr);
        otp.setTtl(10);
        otp.setMobileNo("" + requestDto.getCustomerNumber());
        otp.setId(otp.getMobileNo());
        otpRepository.save(otp);
    }

    @Async("interceptThread")
    public void sendDispatchNotification(OrderEntity orderEntity) {
        try {
            if (StringUtils.isBlank(orderEntity.getItemCourierPartner()) || StringUtils.isBlank(orderEntity.getItemCourierTrackID())) {
                log.info("Not sending message, because track info is not valid...");
                return;
            }
            if (orderEntity.getItemCourierPartner().toLowerCase().replaceAll(" ", "").contains("byhan")) {
                log.info("Not sending message, because dispatched not bu courier...");
                return;
            }
            String newMsg = "From " + getOrgName() + ", \n" + getMsgTemplate();
            final String confirm = messageSenderService.sendSms("" + orderEntity.getCustomerNumber(),
                    newMsg.replace("<COURIER>", orderEntity.getItemCourierPartner()).replace("<TACK_ID>", orderEntity.getItemCourierTrackID()));
            log.info("msg sent confirmation = {}", confirm);

        } catch (Exception e) {
            log.error("Failed to send dispatch update notification for order = {}...", orderEntity);
        }
    }
}
