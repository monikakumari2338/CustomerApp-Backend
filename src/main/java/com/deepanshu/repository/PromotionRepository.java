package com.deepanshu.repository;

import com.deepanshu.modal.Promotion;
import com.deepanshu.user.domain.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
	Optional<Promotion> findByPromotionCode(String promotionCode);

	List<Promotion> findAll();
}
