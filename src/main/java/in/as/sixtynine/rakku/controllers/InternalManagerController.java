package in.as.sixtynine.rakku.controllers;

import in.as.sixtynine.rakku.dtos.DeliveryDetailsDto;
import in.as.sixtynine.rakku.dtos.OrderDispatchDto;
import in.as.sixtynine.rakku.dtos.OrderRequestDto;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.services.OrderService;
import in.as.sixtynine.rakku.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/02/22
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class InternalManagerController {

    private final OrderService orderService;
    private final ProductService productService;

    @CrossOrigin
    @PostMapping("/order")
    public ResponseEntity<OrderEntity> takeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto, Principal principal) throws URISyntaxException, NoSuchAlgorithmException {
        final OrderEntity order = orderService.createOrder(orderRequestDto, principal.getName());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/order/{orderid}")
    public ResponseEntity<OrderEntity> updateDispatch(@RequestBody @Valid OrderDispatchDto orderRequestDto, @PathVariable String orderid,
                                                      Principal principal) throws URISyntaxException, NoSuchAlgorithmException {
        final OrderEntity order = orderService.update(orderRequestDto, orderid, principal.getName());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/order/nondispatched")
    public ResponseEntity<List<DeliveryDetailsDto>> allNonDispatchedOrder() throws URISyntaxException, NoSuchAlgorithmException {
        final List<DeliveryDetailsDto> allNonDispatchedOrder = orderService.getAllNonDispatchedOrder();
        return new ResponseEntity<>(allNonDispatchedOrder, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product, Principal principal) throws URISyntaxException, NoSuchAlgorithmException {
        final Product newItem = productService.createNewItem(product, principal.getName());
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts() throws URISyntaxException, NoSuchAlgorithmException {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.CREATED);
    }

}
