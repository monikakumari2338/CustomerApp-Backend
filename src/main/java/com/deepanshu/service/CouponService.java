package com.deepanshu.service;

import com.deepanshu.modal.Coupon;
import com.deepanshu.modal.Tier;

import java.util.Optional;

public interface CouponService {
    void generateCouponForUser(Long userId);

    Optional<Coupon> getCouponByUserId(Long userId);

    int determineDiscountPercentage(Tier tier);
    void removeCouponByUserId(Long userId);

}
