package tdtu.Proptech.controller.RestAPIController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.SpringCommerce.model.ShoppingCart;
import tdtu.SpringCommerce.response.ApiResponse;
import tdtu.SpringCommerce.service.cart.CartService;

@RestController
@RequestMapping("/api/cart")
public class ApiCartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable Long id) {
        try {
            ShoppingCart cart = cartService.getCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart found", cart));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> clearCart( @PathVariable Long id) {
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart cleared successfully!", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/total-price/{id}")
    public ResponseEntity<ApiResponse> getTotalAmount( @PathVariable Long id) {
        try {
            Double totalPrice = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
