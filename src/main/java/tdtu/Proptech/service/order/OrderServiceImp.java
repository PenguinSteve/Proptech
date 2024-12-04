package tdtu.Proptech.service.order;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdtu.SpringCommerce.dto.OrderDTO;
import tdtu.SpringCommerce.model.*;
import tdtu.SpringCommerce.repository.OrderRepository;
import tdtu.SpringCommerce.repository.ProductRepository;
import tdtu.SpringCommerce.repository.UserRepository;
import tdtu.SpringCommerce.request.PlaceOrderRequest;
import tdtu.SpringCommerce.service.cart.CartService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Orders placeOrder(Long userId, String address) {
        ShoppingCart cart = cartService.getCartByUserId(userId);
        Orders order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        order.setStatus("CHECKOUT");
        order.setDeliveryAddress(address);
        Orders savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Orders createOrder(ShoppingCart cart) {
        Orders order = new Orders();
        order.setUser(cart.getUser());
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Orders order, ShoppingCart cart) {
        return  cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            return  new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }

    private Double calculateTotalAmount(List<OrderItem> orderItemList) {
        return  orderItemList
                .stream()
                .map(item -> item.getPriceAtPurchase() * item.getQuantity())
                .reduce(0.0, Double::sum);
    }

//    @Override
//    public OrderDTO getOrder(Long orderId) {
//        return orderRepository.findById(orderId)
//                .map(this :: convertToDto)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//    }

//    @Override
//    public List<OrderDTO> getUserOrders(Long userId) {
//        List<Orders> orders = orderRepository.findByUserId(userId);
//        return  orders.stream().map(this :: convertToDto).toList();
//    }

    private OrderDTO convertToDto(Orders order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    @Transactional
    @Override
    public Orders placeOrderGuest(PlaceOrderRequest request) {
        User guestUser = new User();
        guestUser.setEmail(request.getEmail());
        guestUser.setName(request.getFirstName() + " " + request.getLastName());
        userRepository.save(guestUser);

        Orders order = new Orders();
        order.setUser(guestUser);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryAddress(request.getAddress());

        List<OrderItem> orderItemList = request.getItems().stream().map(cartItemRequest -> {
            Product product = productRepository.findById(cartItemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            return new OrderItem(
                    order,
                    product,
                    cartItemRequest.getQuantity(),
                    product.getPrice());
        }).toList();

        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        order.setStatus("CHECKOUT");

        // Save the order
        return orderRepository.save(order);
    }
}
