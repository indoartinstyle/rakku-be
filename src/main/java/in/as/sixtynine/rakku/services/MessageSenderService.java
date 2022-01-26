package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.entities.OTP;
import in.as.sixtynine.rakku.repositories.OTPRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */


@Service

@RequiredArgsConstructor
public class MessageSenderService {
    private static final Logger log = LogManager.getLogger(MessageSenderService.class);

    private final OTPRepository otpRepository;

    @Value("${otp.ttl:300}")
    private Long otpTtl;

    public void sendSms(long phoneNumber, int generatedTOTP, String s) {
        log.info("Mobile No: {}, MSG: {}", phoneNumber, s);
        OTP otp = new OTP();
        otp.setOtp(String.valueOf(generatedTOTP));
        otp.setTtl(otpTtl);
        otp.setMobileNo(String.valueOf(phoneNumber));
        otpRepository.save(otp);
    }
}
