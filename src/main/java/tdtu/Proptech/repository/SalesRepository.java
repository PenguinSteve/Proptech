package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Sale;

@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {
}
