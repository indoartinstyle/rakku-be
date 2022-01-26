package in.as.sixtynine.rakku.userservice.controller;


import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.service.UserManagementService;
import in.as.sixtynine.rakku.userservice.tokengeneration.TokenGeneration;
import in.as.sixtynine.rakku.userservice.utils.ELevel;
import in.as.sixtynine.rakku.userservice.utils.UserType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;


@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserRegistrationController {
    private static final Logger log = LogManager.getLogger(UserRegistrationController.class);

    private final UserManagementService userManagementService;
    private final TokenGeneration tokenGeneration;

    @PostMapping("/open/users")
    public ResponseEntity<User> addCustomer(@RequestBody @Valid User customer, @RequestHeader @NotBlank String totp) throws URISyntaxException, NoSuchAlgorithmException {
        customer.setUserType(UserType.BUYER.name());
        customer.setLevel(ELevel.BRONZE.name());
        User user = userManagementService.saveUser(customer, totp);
        HttpHeaders header = new HttpHeaders();
        header.set("token", tokenGeneration.getToken(user));
        return new ResponseEntity<>(user, header, HttpStatus.CREATED);
    }

    @PostMapping("/open/regenerate")
    public ResponseEntity<User> regenerate(@RequestParam String mobilenumber, String otp) throws URISyntaxException, NoSuchAlgorithmException {
        User user = userManagementService.getUserByMobileNumber(mobilenumber);
        HttpHeaders header = new HttpHeaders();
        header.set("token", tokenGeneration.getToken(user, otp));
        return new ResponseEntity<>(user, header, HttpStatus.CREATED);
    }

    @PutMapping("/api/user/add/following")
    public ResponseEntity<User> addFollowing(@RequestParam String customerIds, Principal principal) throws URISyntaxException, NoSuchAlgorithmException {
        User user = userManagementService.addFollowing(principal.getName(), customerIds);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/open/mobileverification")
    public ResponseEntity<Void> validatePhone(@RequestParam String mobilenumber) throws Exception {
        if (StringUtils.isBlank(mobilenumber)) {
            log.error("Empty number");
            return null;
        }
        if (mobilenumber.length() < 10) {
            log.error("Not valid number (should be 10 digit number)");
            return null;
        }
        if (!NumberUtils.isCreatable(mobilenumber)) {
            log.error("Not valid number (only number and 10 digit exactly)");
        }
        userManagementService.generateOtp(Long.parseLong(mobilenumber));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
