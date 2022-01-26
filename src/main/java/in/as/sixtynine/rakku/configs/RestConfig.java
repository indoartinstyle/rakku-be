package in.as.sixtynine.rakku.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate buildRestTemplate() {
        final RestTemplate build = new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(1, ChronoUnit.MINUTES))
                .build();
        return build;
    }
}
