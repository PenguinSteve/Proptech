package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
}
