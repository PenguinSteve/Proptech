package tdtu.Proptech.request;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    private String firstName;
    private String lastName;

    private String address;
    private String email;
    private String phone;
    private List<CartItemRequest> items;
    private Long userId;
}
