package in.as.sixtynine.rakku.configs;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author Sanjay Das (s0d062y), Created on 29/03/22
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class BlobStorageConfig {

    private final BlobServiceClientBuilder clientBuilder;
    @Getter
    private BlobContainerClient blobContainer;

    @PostConstruct
    public void init() throws IOException {
        BlobServiceClient blobServiceClient = clientBuilder.buildClient();
        try {
            this.blobContainer = blobServiceClient.createBlobContainer("images");
        } catch (BlobStorageException ex) {
            if (!ex.getErrorCode().equals(BlobErrorCode.CONTAINER_ALREADY_EXISTS)) {
                throw ex;
            }
            this.blobContainer = blobServiceClient.getBlobContainerClient("images");
        }
    }

    public String upload(String fileName, byte[] buf) throws IOException {
        InputStream dataStream = new ByteArrayInputStream(buf);
        final BlobClient blobClient = blobContainer.getBlobClient(fileName);
        blobClient.upload(dataStream, buf.length);
        return blobClient.getBlobUrl();
    }
}
