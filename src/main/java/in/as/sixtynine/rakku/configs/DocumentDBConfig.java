package in.as.sixtynine.rakku.configs;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class DocumentDBConfig {

    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Getter
    @Value("${azure.cosmos.database}")
    private String dbName;


}