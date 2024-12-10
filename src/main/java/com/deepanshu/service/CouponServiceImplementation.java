package com.deepanshu.service;

import com.deepanshu.modal.*;
import com.deepanshu.repository.CartRepository;
import org.springframework.stereotype.Service;
import com.deepanshu.repository.CouponRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImplementation implements CouponService {
    private final TierService tierService;
    private final UserService userService;
    private final CouponRepository couponRepository;

    private final CartService cartService;
    private final CartRepository cartRepository;

    public CouponServiceImplementation(TierService tierService, UserService userService, CouponRepository couponRepository, CartService cartService,CartRepository cartRepository) {
        this.tierService = tierService;
        this.userService = userService;
        this.couponRepository = couponRepository;
        this.cartService = cartService;
        this.cartRepository=cartRepository;
    }

    @Override
    public void generateCouponForUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return;
        }
        Tier tier = tierService.determineTier(userId);
        int discountPercentage = determineDiscountPercentage(tier);
        if (discountPercentage > 0 && !user.isDiscountApplied()) {
            cartService.applyDiscountToUserCart(userId, discountPercentage);
            user.setDiscountApplied(true);
            userService.saveUser(user);
        }
        Coupon coupon = new Coupon();
        coupon.setUser(user);
        coupon.setDiscountPercentage(discountPercentage);
        couponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> getCouponByUserId(Long userId) {
        return couponRepository.findByUser_Id(userId);
    }

    @Override
    public int determineDiscountPercentage(Tier tier) {
        switch (tier) {
            case SILVER:
                return 3;
            case GOLD:
                return 8;
            case PLATINUM:
                return 12;
            default:
                return 0;
        }
    }

    @Override
    public void removeCouponByUserId(Long userId) {
        List<Coupon> coupons = couponRepository.findByUserId(userId);
        for (Coupon coupon : coupons) {
            int discountPercentage = coupon.getDiscountPercentage();
            Cart cart = cartService.findUserCart(userId);
            if (cart != null) {
                removeDiscounts(cart, discountPercentage);
            }
            couponRepository.delete(coupon);
        }
    }


    private void removeDiscounts(Cart cart, int discountPercentage) {
        for (CartItem cartItem : cart.getCartItems()) {
            int originalPrice = cartItem.getProduct().getDiscountedPrice(); // Assuming you have the original price stored in the Product object
            int discountedPrice = cartItem.getDiscountedPrice();
            int newDiscountedPrice = (int) (discountedPrice / (1 - (discountPercentage / 100.0)));
            int discountAmount = discountedPrice - newDiscountedPrice;
            cartItem.setDiscountedPrice(originalPrice); // Resetting to original price
            cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice() + discountAmount); // Adjusting total price
        }
        cartRepository.save(cart);
    }

}
