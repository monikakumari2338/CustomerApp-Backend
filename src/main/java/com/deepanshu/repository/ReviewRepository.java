package com.deepanshu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Review;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@Query("Select r from Review r where r.product.id=:productId")
	public List<Review> getAllProductsReview(@Param("productId") Long productId);

	public Review findByProductAndUser(Product product, User user);
}
