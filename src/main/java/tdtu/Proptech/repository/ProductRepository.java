package tdtu.Proptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tdtu.SpringCommerce.model.Product;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContaining(@Param("name") String name);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE " +
            "(:category IS NULL OR c.name = :category) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:color IS NULL OR p.color = :color)")
    List<Product> findByCriteria(@Param("category") String category,
                                 @Param("brand") String brand,
                                 @Param("color") String color);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE " +
            "(:category IS NULL OR c.name = :category) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:color IS NULL OR p.color = :color) " +
            "ORDER BY p.price ASC")
    List<Product> findByCriteriaOrderByPriceAsc(@Param("category") String category,
                                                @Param("brand") String brand,
                                                @Param("color") String color);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE " +
            "(:category IS NULL OR c.name = :category) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:color IS NULL OR p.color = :color) " +
            "ORDER BY p.price DESC")
    List<Product> findByCriteriaOrderByPriceDesc(@Param("category") String category,
                                                 @Param("brand") String brand,
                                                 @Param("color") String color);
}