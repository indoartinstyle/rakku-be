package in.as.sixtynine.rakku.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import in.as.sixtynine.rakku.entities.OTP;
import in.as.sixtynine.rakku.repositories.OTPRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

    @Value("${sms.sid}")
    private String ACCOUNT_SID;

    @Value("${sms.token}")
    private String AUTH_TOKEN;

    @Value("${sms.from}")
    private String FROM;

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 100))
    public String sendSms(String phoneNumber, String msg) {
        phoneNumber = phoneNumber.length() == 10 ? ("91" + phoneNumber) : phoneNumber;
        log.info("sending {}, to {}", msg, phoneNumber);
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(FROM);
        Message message = Message.creator(to, from, msg).create();
        log.info("Msg send status: {}", message.getStatus().name());
        return message.getSid();
    }

    public void sendSms(long phoneNumber, int generatedTOTP, String s) {
        log.info("Mobile No: {}, MSG: {}", phoneNumber, s);
        OTP otp = new OTP();
        otp.setOtp(String.valueOf(generatedTOTP));
        otp.setTtl(otpTtl);
        otp.setMobileNo(String.valueOf(phoneNumber));
        otp.setId(otp.getMobileNo());
        otpRepository.save(otp);
        sendSms("+" + phoneNumber, s);
    }
}
