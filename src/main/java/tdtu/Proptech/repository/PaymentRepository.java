package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
