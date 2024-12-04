package tdtu.Proptech.dto;

import lombok.Data;
import tdtu.SpringCommerce.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private String deliveryAddress;
    private List<OrderItem> items;
}
