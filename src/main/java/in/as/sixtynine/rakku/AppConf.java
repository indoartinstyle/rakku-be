package in.as.sixtynine.rakku;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @Author Sanjay Das (s0d062y), Created on 25/01/22
 */

@Configuration
@RequiredArgsConstructor
public class AppConf {

    private final Environment environment;

    public boolean isLocal() {
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(prof -> prof.equalsIgnoreCase("dev"));
    }
}
