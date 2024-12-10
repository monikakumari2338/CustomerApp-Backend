package com.deepanshu.service;

import com.deepanshu.exception.ProductException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Cart;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.User;
import com.deepanshu.request.AddItemRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CartService {

    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException;

    public Cart findUserCart(Long userId);

    void clearCart(Long id);

    void applyDiscountToUserCart(Long userId, int discountPercentage);

    void setExpressDelivery(Long userId, boolean expressDelivery);

    public BigDecimal calculateDiscount(Long userId, String promoCode);

    void removeDiscount(Long userId);

    BigDecimal getDiscountAmount(Long userId);

    void removeExpressDelivery(Long userId);

    BigDecimal applyBirthdayDiscount(Long userId) throws UserException;

    BigDecimal applyAnniversaryDiscount(Long userId) throws UserException;

    void applyBogoPromotion(Long userId) throws ProductException;

    void applyBestItemWisePromotion(Long userId) throws ProductException;

    void applyBestTransactionWisePromotion(Long userId) throws ProductException;


    //apply 5% OFF on if cartValue>=1500, transactionWise promotion
    double applyFivePercentOFFTransactionWise(Long userId,boolean isForCheckBestPromotion);

    //apply Rs.500 OFF on if cartValue>=1500, transactionWise promotion
    double applyFiveHundredOFFTransactionWise(Long userId,boolean isForCheckBestPromotion);

    //apply Best Promotion on cart
    void applyBestPromotionOnCart(Long userId);

    //apply itemWise 200 OFF on selectedItems promotion when added in the cart
    double applyItemWiseTwoHundredOFFOnProduct(Long userId,boolean isForCheckBestPromotion);

    //apply itemWise 20% OFF on selectedItems promotion when added in the cart
    double applyItemWiseTwentyPercentOFFOnProduct(Long userId,boolean isForCheckBestPromotion);

    //apply anniversary promotion applied, if cartValue>=1500, transactionWise promotion
    double anniversaryPromotionOnCart(Long userId,boolean isForCheckBestPromotion);

    //apply birthday promotion applied, if cartValue>=1500, transactionWise promotion
    double birthdayPromotionOnCart(Long userId,boolean isForCheckBestPromotion);

    //boGo promotion on selected products
    double createBoGoPromotion(Long userId,boolean isForCheckBestPromotion);
}
