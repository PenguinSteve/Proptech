package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Property;

@Repository
public interface ListingRepository extends JpaRepository<Property, Long> {
}
