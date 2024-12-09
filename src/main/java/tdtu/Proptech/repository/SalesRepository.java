package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Sales;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByPropertyStatus(String status);
    Optional<Sales> findByPropertyId(Long id);
}
