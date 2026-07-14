package org.northernarc.customerproduct.repository;

import org.northernarc.customerproduct.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> findByCategory(@Param("category") String category);

    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    List<Product> findByBrand(@Param("brand") String brand);

    @Query("""
       SELECT p
       FROM Product p
       WHERE p.price BETWEEN :minPrice AND :maxPrice
       """)
    List<Product> findByPriceRange(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);

    @Query("""
       SELECT p
       FROM Product p
       ORDER BY p.price ASC
       """)
    List<Product> sortByPriceAsc();

    @Query("""
       SELECT p
       FROM Product p
       ORDER BY p.price DESC
       """)
    List<Product> sortByPriceDesc();

    @Query("""
       SELECT p
       FROM Product p
       ORDER BY p.name ASC
       """)
    List<Product> sortByName();

    @Query("""
       SELECT p
       FROM Product p
       ORDER BY p.category ASC
       """)
    List<Product> sortByCategory();

    @Query("""
       SELECT p
       FROM Product p
       WHERE LOWER(p.name)
       LIKE LOWER(CONCAT('%',:keyword,'%'))
       """)
    List<Product> searchProducts(
            @Param("keyword") String keyword);
}
