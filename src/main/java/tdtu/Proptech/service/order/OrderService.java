package tdtu.Proptech.service.order;

import tdtu.SpringCommerce.model.Orders;
import tdtu.SpringCommerce.request.PlaceOrderRequest;

public interface OrderService {
    Orders placeOrder(Long userId, String address);
//    OrderDTO getOrder(Long orderId);
//    List<OrderDTO> getUserOrders(Long userId);

    Orders placeOrderGuest(PlaceOrderRequest request);
}
