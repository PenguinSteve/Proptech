package tdtu.Proptech.controller.RestAPIController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.SpringCommerce.model.Orders;
import tdtu.SpringCommerce.request.PlaceOrderRequest;
import tdtu.SpringCommerce.response.ApiResponse;
import tdtu.SpringCommerce.service.order.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody PlaceOrderRequest request) {
        try {
            Orders order;
            if(request.getUserId() == null){
                order = orderService.placeOrderGuest(request);
            }
            else{
                order =  orderService.placeOrder(request.getUserId(), request.getAddress());
            }
            return ResponseEntity.ok(new ApiResponse("Place order successfully!", order));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occured!", e.getMessage()));
        }
    }

}
