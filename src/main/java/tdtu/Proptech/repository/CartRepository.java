package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdtu.SpringCommerce.model.ShoppingCart;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserId(Long userId);
}
