package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tdtu.Proptech.model.Property;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query("SELECT p FROM Property p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:minPrice IS NULL OR :maxPrice IS NULL OR (p.price BETWEEN :minPrice AND :maxPrice)) AND " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:address IS NULL OR p.address LIKE %:address%)")
    List<Property> findPropertiesByCriteria(@Param("type") String type,
                                            @Param("minPrice") Double minPrice,
                                            @Param("maxPrice") Double maxPrice,
                                            @Param("name") String name,
                                            @Param("address") String address);

    List<Property> findByType(String type);
}
