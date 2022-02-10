package in.as.sixtynine.rakku.services;

import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String PREFIX = "PRODUCT-";

    private final ProductRepository productRepository;

    public Product createNewItem(Product product, String name) {
        product.setId(PREFIX + productRepository.getSeqNum());
        product.setItemModelName(product.getItemModelName() + "-" + product.getItemSize() + "-" + product.getItemColor());
        product.setCreatedTime(System.currentTimeMillis());
        product.setCreatedBy(name);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllAvailableProducts();
    }

}
