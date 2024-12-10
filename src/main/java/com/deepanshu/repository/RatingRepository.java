package com.deepanshu.repository;

import java.util.List;
import java.util.Optional;

import com.deepanshu.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProductId(Long productId);
    //check the user who already rated for a product
    List<Rating>findByUserIdAndProductId(Long userId,Long productId);
}
