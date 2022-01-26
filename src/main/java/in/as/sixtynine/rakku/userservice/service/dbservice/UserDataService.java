package in.as.sixtynine.rakku.userservice.service.dbservice;


import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import in.as.sixtynine.rakku.AppConf;
import in.as.sixtynine.rakku.entities.OTP;
import in.as.sixtynine.rakku.repositories.OTPRepository;
import in.as.sixtynine.rakku.services.MessageSenderService;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.crypto.KeyGenerator;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserDataService {
    private static final Logger log = LogManager.getLogger(UserDataService.class);

    private final AppConf conf;
    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final MessageSenderService messageSenderService;


    public User saveUser(User user, String totp) {
        if (conf.isLocal()) {
            return userRepository.save(user);
        }
        final Optional<OTP> byId = otpRepository.findById("" + user.getPhoneNumber());
        if (!byId.isPresent()) {
            throw new RuntimeException("Otp mismatch/expired ! Registration failed");
        }
        if (totp.equals(byId.get().getOtp())) {
            return userRepository.save(user);
        }
        throw new RuntimeException("Otp mismatch/expired ! Registration failed");
    }

    public void saveSMSOtp(long phoneNumber) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
            final Key key;
            {
                final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
                keyGenerator.init(160);
                key = keyGenerator.generateKey();
            }
            int generatedTOTP = totp.generateOneTimePassword(key, Instant.now());
            log.info("TOTP generated for for phone number = {}", phoneNumber);
            messageSenderService.sendSms(phoneNumber, generatedTOTP, "Your otp for HealthYantra Registration is " + generatedTOTP);
        } catch (Exception e) {
            log.error("Error in saveSMSOtp() - Error = {}", e.getMessage());
        }
    }

    public User addFollowing(String name, String customerIds) {
        final Optional<User> byId = userRepository.findById(name);
        if (!byId.isPresent()) {
            log.info("Not found any user for id ={}", name);
            throw new RuntimeException("");
        }
        final User entity = byId.get();
        if (CollectionUtils.isEmpty(entity.getFollowing())) {
            Set<String> followingSet = new HashSet<>();
            Arrays.stream(customerIds.split(",")).forEach(followingSet::add);
            entity.setFollowing(followingSet);
        } else {
            Arrays.stream(customerIds.split(",")).forEach(entity.getFollowing()::add);
        }
        return userRepository.save(entity);
    }

    public User getUserByID(String id) {
        return userRepository.findById(id).get();
    }

    public User getUserByMobileNumber(String mobilenumber) {
        if (StringUtils.isBlank(mobilenumber)) {
            log.error("Empty number");
            return null;
        }
        if (mobilenumber.length() < 10) {
            log.error("Not valid number (should be 10 digit number)");
            return null;
        }
        if (!NumberUtils.isCreatable(mobilenumber)) {
            log.error("Not valid number (only number and >= 10 digit)");
        }
        return userRepository.findByPhoneNumber(Long.parseLong(mobilenumber)).get(0);
    }
}
