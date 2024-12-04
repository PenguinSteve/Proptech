package tdtu.Proptech.service.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tdtu.SpringCommerce.model.CartItem;
import tdtu.SpringCommerce.model.Product;
import tdtu.SpringCommerce.model.ShoppingCart;
import tdtu.SpringCommerce.repository.CartItemRepository;
import tdtu.SpringCommerce.repository.CartRepository;
import tdtu.SpringCommerce.service.product.ProductService;

@Service
public class CartItemServiceImpl implements CartItemService{

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private CartRepository cartRepository;


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        ShoppingCart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst().orElse(new CartItem());

        if(cartItem.getCartItemId() == null){
            cartItem.setShoppingCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        ShoppingCart cart = cartService.getCart(cartId);
        CartItem removedItem = getCartItem(cartId, productId);
        cart.removeItem(removedItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        ShoppingCart cart = cartService.getCart(cartId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        Double totalAmount = cart.getCartItems()
                .stream().map(CartItem ::getTotalPrice)
                .reduce(0.0, Double::sum);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        ShoppingCart cart = cartService.getCart(cartId);
        return  cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst().orElseThrow(() -> new RuntimeException("Item not found"));
    }
}
