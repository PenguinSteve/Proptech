package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdtu.SpringCommerce.model.Orders;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(Long userId);
}
