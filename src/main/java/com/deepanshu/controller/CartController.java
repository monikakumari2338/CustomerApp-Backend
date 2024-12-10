package com.deepanshu.controller;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.modal.Order;
import com.deepanshu.request.ExpressDeliveryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.ProductException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Cart;
import com.deepanshu.modal.User;
import com.deepanshu.request.AddItemRequest;
import com.deepanshu.response.ApiResponse;
import com.deepanshu.service.CartService;
import com.deepanshu.service.UserService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "https://localhost:8081")
public class CartController {

    private CartService cartService;
    private UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.findUserCart(user.getId());

        System.out.println("cart - " + cart.getUser().getEmail());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {

        User user = userService.findUserProfileByJwt(jwt);

        cartService.addCartItem(user.getId(), req);

        ApiResponse res = new ApiResponse("Item Added To Cart Successfully", true);

        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);

    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);

        cartService.clearCart(user.getId());

        ApiResponse response = new ApiResponse("Cart cleared successfully", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{userId}/express-delivery")
    public ResponseEntity<String> setExpressDelivery(@PathVariable Long userId, @RequestBody ExpressDeliveryRequest request) {
        cartService.setExpressDelivery(userId, request.isExpressDelivery());
        return ResponseEntity.ok("Express delivery option updated.");
    }

    @PostMapping("/calculate-discount")
    public ResponseEntity<String> calculateDiscountForUser(@RequestParam Long userId, @RequestParam String promoCode) {
        BigDecimal totalDiscount = cartService.calculateDiscount(userId, promoCode);
        return ResponseEntity.ok("Total Discount Calculated: " + totalDiscount);
    }

    @PostMapping("/removeDiscount")
    public ResponseEntity<String> removeDiscount(@RequestParam Long userId) {
        cartService.removeDiscount(userId);
        return ResponseEntity.ok("Discount removed successfully.");
    }

    @GetMapping("/getDiscountAmount")
    public ResponseEntity<BigDecimal> getDiscountAmount(@RequestParam Long userId) {
        BigDecimal discountAmount = cartService.getDiscountAmount(userId);
        return ResponseEntity.ok(discountAmount);
    }

    @DeleteMapping("/{userId}/express-delivery")
    public void removeExpressDelivery(@PathVariable Long userId) {
        cartService.removeExpressDelivery(userId);
    }

    @PostMapping("/apply-birthday-discount")
    public ResponseEntity<String> applyBirthdayDiscount(@RequestParam Long userId) throws UserException {
        BigDecimal totalDiscount = cartService.applyBirthdayDiscount(userId);
        return ResponseEntity.ok("Total discount calculated" + totalDiscount);
    }

    @PostMapping("/apply-anniversary-discount")
    public ResponseEntity<String> applyAnniversaryDiscount(@RequestParam Long userId) throws UserException {
        BigDecimal totalDiscount = cartService.applyAnniversaryDiscount(userId);
        return ResponseEntity.ok("Total discount calculated" + totalDiscount);
    }

    @PostMapping("/apply-bogo/{userId}")
    public ResponseEntity<String> applyBogoPromotion(@PathVariable Long userId) {
        try {
            cartService.applyBogoPromotion(userId);
            return ResponseEntity.ok("BOGO promotion applied successfully.");
        } catch (ProductException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to apply BOGO promotion: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user ID: " + userId);
        }
    }
    @PostMapping("/apply-best-item-wise-promotion")
    public ResponseEntity<?> applyBestItemWisePromotion(
            @RequestParam Long userId) {
        try {
            cartService.applyBestItemWisePromotion(userId);
            return ResponseEntity.ok("Best item-wise promotion applied successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error applying best item-wise promotion: " + e.getMessage());
        }
    }

    @PostMapping("/apply-best-transaction-wise-promotion")
    public ResponseEntity<?> applyBestTransactionWisePromotion(
            @RequestParam Long userId) {
        try {
            cartService.applyBestTransactionWisePromotion(userId);
            return ResponseEntity.ok("Best transaction-wise promotion applied successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error applying best transaction-wise promotion: " + e.getMessage());
        }
    }
}
