package in.as.sixtynine.rakku.userservice.tokengeneration;


import in.as.sixtynine.rakku.AppConf;
import in.as.sixtynine.rakku.repositories.OTPRepository;
import in.as.sixtynine.rakku.security.jwt.common.JwtAuthenticationConfig;
import in.as.sixtynine.rakku.userservice.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenGeneration {
    private static final Logger log = LogManager.getLogger(TokenGeneration.class);


    public static final String AUTHORITIES = "authorities";
    public static final String USER = "user";

    private final JwtAuthenticationConfig config;
    private final OTPRepository otpRepository;
    private final AppConf conf;


    public String getToken(User user) {
        log.info("Generating token for user = {}", user);
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getId())
                .claim(AUTHORITIES, user.getRoles())
                .claim(USER, user)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(config.getExpiration())))
                // TODO Need to implement RSA algorithm with key
                .signWith(SignatureAlgorithm.HS256, config.getSecret().getBytes())
                .compact();
    }

    public String getToken(User user, String otp) {
        if (conf.isLocal() || otpRepository.findById("" + user.getPhoneNumber()).get().getOtp().equalsIgnoreCase(otp)) {
            return getToken(user);
        }
        throw new RuntimeException(" Otp mismatch");
    }


}
