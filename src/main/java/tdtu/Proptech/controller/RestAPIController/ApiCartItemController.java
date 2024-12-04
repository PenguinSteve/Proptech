package tdtu.Proptech.controller.RestAPIController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.SpringCommerce.request.CartItemRequest;
import tdtu.SpringCommerce.response.ApiResponse;
import tdtu.SpringCommerce.service.cart.CartItemService;
import tdtu.SpringCommerce.service.cart.CartService;

@RestController
@RequestMapping("/api/cart_item")
public class ApiCartItemController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody CartItemRequest request) {
        try {
            Long cartId = request.getCartId();
            Long productId = request.getProductId();
            Long userId = request.getUserId();
            int quantity = request.getQuantity();

            if (request.getCartId() == null) {
                cartId = cartService.initializeNewCart(userId);
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", cartId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestBody CartItemRequest request) {
        try {
            Long cartId = request.getCartId();
            Long productId = request.getProductId();

            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Item removed Successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("")
    public  ResponseEntity<ApiResponse> updateItemQuantity(@RequestBody CartItemRequest request) {
        try {
            Long cartId = request.getCartId();
            Long productId = request.getProductId();
            int quantity = request.getQuantity();

            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
