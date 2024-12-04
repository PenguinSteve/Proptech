package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdtu.SpringCommerce.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByShoppingCartId(Long id);
}
