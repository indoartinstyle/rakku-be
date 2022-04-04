package in.as.sixtynine.rakku.services;

import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosStoredProcedureProperties;
import com.azure.cosmos.models.CosmosStoredProcedureRequestOptions;
import com.azure.cosmos.models.CosmosStoredProcedureResponse;
import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.as.sixtynine.rakku.configs.DocumentDBConfig;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.entities.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import static in.as.sixtynine.rakku.constants.DBConstants.CORE_CONTAINER;

/**
 * @Author Sanjay Das (s0d062y), Created on 04/04/22
 */


@Log4j2
@Service
@RequiredArgsConstructor
public class OrderProductTransaction {

    protected static final String TRANSACTION_STORED_PROC = "updateOrderAndStock";
    private final DocumentDBConfig documentDBConfig;
    private final CosmosClientBuilder cosmosClientBuilder;

    private CosmosContainer container;

    @PostConstruct
    public void setUp() throws Exception {
        CosmosClient client = cosmosClientBuilder.buildClient();
        CosmosDatabase database = client.getDatabase(documentDBConfig.getDbName());
        container = database.getContainer(CORE_CONTAINER);
        loadTransactionStoredProc();
    }

    private void loadTransactionStoredProc() throws IOException {
        File file = getResourceFile("storedproc/rbox.cosmosdb.storeproc.update.order.js");
        String sprocBody = new String(Files.readAllBytes(file.toPath()));
        log.info("Stored proc = {}", sprocBody);
        CosmosStoredProcedureProperties storedProcedureDef = new CosmosStoredProcedureProperties(TRANSACTION_STORED_PROC, sprocBody);
        try {
            log.info("Trying to create transactional stored proc....");
            container.getScripts().createStoredProcedure(storedProcedureDef, new CosmosStoredProcedureRequestOptions());
        } catch (CosmosException ex) {
            if (ex.getStatusCode() == HttpStatus.SC_CONFLICT && ex.getMessage().contains("Resource with specified id, name, or unique index already exists")) {
                log.info("stored proc present...");
                return;
            }
            log.error("Found CosmosException error while creating stored proc\nError=> {}", ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error("Found error while creating stored proc\nError=> {}", e.getMessage());
            throw e;
        }
    }

    private File getResourceFile(final String fileName) {
        URL url = this.getClass()
                .getClassLoader()
                .getResource(fileName);

        if (url == null) {
            final String s = fileName + " is not found 1";
            log.error(s);
            throw new IllegalArgumentException(s);
        }
        File file = new File(url.getFile());
        return file;
    }


    @Retryable(value = {Exception.class})
    public OrderEntity updateOrderProduct(OrderEntity orderEntity, List<Product> products) throws JsonProcessingException {
        try {
            CosmosStoredProcedureRequestOptions options = new CosmosStoredProcedureRequestOptions();
            options.setPartitionKey(new PartitionKey(orderEntity.getKey()));
            CosmosStoredProcedureResponse executeResponse = container.getScripts()
                    .getStoredProcedure(TRANSACTION_STORED_PROC)
                    .execute(List.of(orderEntity, products), options);
            if (null != executeResponse) {
                final ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return mapper.readValue(executeResponse.getResponseAsString(), OrderEntity.class);
            }
        } catch (Exception e) {
            log.error("returnProductTransactionOperation() - {}", e.getMessage());
            throw e;
        }
        return null;
    }
}
