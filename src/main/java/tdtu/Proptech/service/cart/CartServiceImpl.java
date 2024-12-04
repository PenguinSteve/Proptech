package tdtu.Proptech.service.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tdtu.SpringCommerce.model.ShoppingCart;
import tdtu.SpringCommerce.model.User;
import tdtu.SpringCommerce.repository.CartItemRepository;
import tdtu.SpringCommerce.repository.CartRepository;
import tdtu.SpringCommerce.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ShoppingCart getCart(Long id) {
        ShoppingCart cart = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        Double totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        ShoppingCart cart = getCart(id);
        cartItemRepository.deleteAllByShoppingCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    public Double getTotalPrice(Long id){
        ShoppingCart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        newCart = cartRepository.save(newCart);
        user.setShoppingCart(newCart);
        userRepository.save(user);
        return newCart.getId();
    }

    @Override
    public ShoppingCart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
