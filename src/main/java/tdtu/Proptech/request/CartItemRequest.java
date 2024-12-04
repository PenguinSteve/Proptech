package tdtu.Proptech.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long cartId;
    private Long productId;
    private Long userId;
    private int quantity;
}
