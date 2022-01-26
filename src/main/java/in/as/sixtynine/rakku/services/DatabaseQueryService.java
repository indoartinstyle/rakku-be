package in.as.sixtynine.rakku.services;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.azure.spring.data.cosmos.core.mapping.Container;
import in.as.sixtynine.rakku.configs.DocumentDBConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Author Sanjay Das (s0d062y), Created on 25/01/22
 */


@Service
@RequiredArgsConstructor
public class DatabaseQueryService {

    private static final Logger log = LogManager.getLogger(DatabaseQueryService.class);

    private final CosmosClientBuilder cosmosClientBuilder;
    private final DocumentDBConfig documentDBConfig;

    @Getter
    @Setter
    private CosmosDatabase database;


    @PostConstruct
    public void init() throws RuntimeException {
        final CosmosClient cosmosClient = cosmosClientBuilder.buildClient();
        this.database = cosmosClient.getDatabase(documentDBConfig.getDbName());
    }

    public <T> CosmosPagedIterable<T> runQuery(CosmosQueryRequestOptions options, String query, Class<T> clazz) {
        log.info("Running query = {}", query);
        return getDatabase().getContainer(clazz.getAnnotation(Container.class).containerName()).queryItems(query, options, clazz);
    }

}
