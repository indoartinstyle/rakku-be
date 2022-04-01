package in.as.sixtynine.rakku.controllers;

import in.as.sixtynine.rakku.dtos.DeliveryDetailsDto;
import in.as.sixtynine.rakku.dtos.ReturnData;
import in.as.sixtynine.rakku.entities.OrderEntity;
import in.as.sixtynine.rakku.entities.Product;
import in.as.sixtynine.rakku.services.OrderService;
import in.as.sixtynine.rakku.services.ProductService;
import in.as.sixtynine.rakku.services.StorageService;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @Author Sanjay Das (s0d062y), Created on 08/02/22
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class IAISController {

    private final OrderService orderService;
    private final ProductService productService;
    private final StorageService storageService;
    private final UserManagementService userManagementService;


    @CrossOrigin
    @GetMapping("/order/nondispatched")
    public ResponseEntity<List<DeliveryDetailsDto>> allNonDispatchedOrder() throws URISyntaxException, NoSuchAlgorithmException {
        final List<DeliveryDetailsDto> allNonDispatchedOrder = orderService.getAllNonDispatchedOrder();
        return new ResponseEntity<>(allNonDispatchedOrder, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts() throws URISyntaxException, NoSuchAlgorithmException {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PostMapping(value = "/user/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> uploadUserAvatar(@RequestPart Optional<MultipartFile> file, Principal principal) throws Exception {
        final MultipartFile multipartFile = file.get();
        if (!multipartFile.getContentType().contains("image")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final User user = userManagementService.geLoggedInUser(principal);
        storageService.userAvatarUpdate(user, multipartFile.getBytes());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/product/all")
    public ResponseEntity<List<Product>> getAllProductsV2() {
        return new ResponseEntity<>(productService.getAllProductsV2(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/product/return")
    public ResponseEntity<OrderEntity> returnProducts(@RequestParam String orderid, @RequestBody ReturnData returnDto, Principal principal) {
        final User user = userManagementService.geLoggedInUser(principal);
        return new ResponseEntity<OrderEntity>(orderService.returnProducts(user, orderid, returnDto), HttpStatus.CREATED);
    }

}
