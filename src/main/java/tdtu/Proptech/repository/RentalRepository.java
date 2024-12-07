package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
}
