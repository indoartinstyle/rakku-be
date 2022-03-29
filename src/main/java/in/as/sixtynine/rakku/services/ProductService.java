package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String PREFIX = "PRODUCT-";

    private final ProductRepository productRepository;
    private final StorageService storageService;

    public Product createNewItem(Product product, String name) {
        log.info("Creating product, details=[{}], by {}", product, name);
        if (product.getCost() <= 0D) {
            throw new RuntimeException("Cost cannot be < 0");
        }
        product.setId(PREFIX + productRepository.getSeqNum());
        product.setItemModelName(product.getItemModelName() + "-" + product.getItemSize() + "-" + product.getItemColor());
        product.setCreatedTime(System.currentTimeMillis());
        product.setCreatedBy(name);
        return productRepository.save(product);
    }

    public Product updateStock(Product product, String name) {
        log.info("Updating product stocks, details=[{}], by {}", product, name);
        if (product.getStock() <= 0) {
            throw new RuntimeException("Cost cannot be < 0");
        }
        final Optional<Product> byId = productRepository.findById(product.getId());
        final Product toBeUpdated = byId.get();
        final int currentStock = toBeUpdated.getStock() < 0 ? 0 : toBeUpdated.getStock();
        toBeUpdated.setStock(currentStock + product.getStock());
        toBeUpdated.setUpdatedBy(name);
        return productRepository.save(toBeUpdated);
    }

    public List<Product> getAllProducts() {
        log.info("Getting available products...");
        return productRepository.getAllAvailableProducts();
    }

    public List<Product> getAllProductsV2() {
        log.info("Getting all the products...");
        return productRepository.getAllProduct();
    }

    public Product uploadProductImage(String productID, byte[] file) throws IOException {
        log.info("Uploading image for product={}", productID);
        final Product product = productRepository.findById(productID).get();
        storageService.productImageUpload(product, file);

        return productRepository.save(product);
    }

}
