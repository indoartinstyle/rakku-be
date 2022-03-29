package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.configs.BlobStorageConfig;
import in.as.sixtynine.rakku.entities.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author Sanjay Das (s0d062y), Created on 29/03/22
 */

@Service
@Log4j2
@RequiredArgsConstructor
public class StorageService {
    private final BlobStorageConfig config;

    public void productImageUpload(Product product, byte[] file) throws IOException {
        try {
            final String fileName = product.getId() + System.nanoTime() + ".jpg";
            product.getImgUrl().add(config.upload(fileName, file));
        } catch (IOException e) {
            log.error("Error while uploading product image..\n Error ={}", e.getMessage());
            throw e;
        }
    }
}
