package com.deepanshu.repository;

import com.deepanshu.modal.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByUser_Id(Long userId);
    List<Coupon> findByUserId(Long userId);
}
