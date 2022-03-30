package in.as.sixtynine.rakku;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author Sanjay Das (s0d062y), Created on 30/03/22
 */

@Configuration
public class AsyncConfig {

    @Bean(name = "interceptThread")
    public Executor threadPoolTaskExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
