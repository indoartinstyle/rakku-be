package in.as.sixtynine.rakku;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/*@Profile({"dev", "local"})*/
public class DevCorsConfiguration implements WebMvcConfigurer {
    private static final Logger log = LogManager.getLogger(DevCorsConfiguration.class);
/*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Found dev/local deployment, allowing cors");
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }*/
}