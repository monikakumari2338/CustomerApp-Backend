package com.deepanshu.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.deepanshu.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Product;
import com.deepanshu.modal.Promotion;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE LOWER(p.category.name) = :category")
	List<Product> findByCategory(@Param("category") String category);

	@Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE %:query% OR LOWER(p.description) LIKE %:query% OR LOWER(p.brand) LIKE %:query% OR LOWER(p.category.name) LIKE %:query%")
	List<Product> searchProduct(@Param("query") String query);

	@Query("SELECT p FROM Product p " + "WHERE (p.category.name = :category OR :category = '') "
			+ "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) "
			+ "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount) " + "ORDER BY "
			+ "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, "
			+ "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
	List<Product> filterProducts(@Param("category") String category, @Param("minPrice") Integer minPrice,
			@Param("maxPrice") Integer maxPrice, @Param("minDiscount") Integer minDiscount, @Param("sort") String sort);

	List<Product> findByBrand(String brandName);

	Collection<? extends Product> findByCategoryOrBrand(Category category, String brand);

	List<Product> findByCategoryIn(Set<String> complementaryCategories);

	List<Product> findByDescriptionContaining(String param);

	@Query(value = "SELECT * FROM product ORDER BY count_users_rated_product_five_stars DESC LIMIT 4", nativeQuery = true)
	List<Product> findTop4ByCountUsersRatedProductFiveStars();

	@Query(value = "SELECT * FROM product ORDER BY count_users_rated_product_four_stars DESC LIMIT 2", nativeQuery = true)
	List<Product> findTop2ByCountUsersRatedProductFourStars();

//	@Query("SELECT p FROM Product p JOIN FETCH p.details d WHERE d.sku = :sku")
//	Product findProductBySku(@Param("sku") String sku);

	@Query("SELECT p FROM Product p JOIN p.details d WHERE d.sku = :sku")
	Product findProductBySku(@Param("sku") String sku);

	// Optional<Product> findById(Long id);
}
