package com.deepanshu.controller;

import com.deepanshu.modal.Coupon;
import com.deepanshu.service.CouponService;
import com.deepanshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "https://localhost:8081")
public class CouponController {

    private final CouponService couponService;
    private final UserService userService;

    @Autowired
    public CouponController(CouponService couponService, UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    //apply coupon
    @PostMapping("/generate/{userId}")
    public ResponseEntity<Void> generateCouponForUser(@PathVariable Long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        couponService.generateCouponForUser(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Coupon>> getCouponByUserId(@PathVariable Long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        Optional<Coupon> coupon = couponService.getCouponByUserId(userId);

        if (coupon != null) {
            return ResponseEntity.ok(coupon);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{userId}/coupon")
    public String removeCoupon(@PathVariable Long userId) {
        couponService.removeCouponByUserId(userId);
        return "Coupon removed successfully";
    }
}