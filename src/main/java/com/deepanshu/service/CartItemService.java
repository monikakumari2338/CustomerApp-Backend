package com.deepanshu.service;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.exception.ProductException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Cart;
import com.deepanshu.modal.CartItem;
import com.deepanshu.modal.Product;
import com.deepanshu.request.AddItemRequest;

import java.util.List;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem) throws UserException;

    public CartItem updateCartItem(Long userId, Long id,  AddItemRequest req) throws CartItemException, UserException, ProductException;

    public CartItem isCartItemExist(Cart cart, String sku, Long userId);

    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException, ProductException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;

    void updateDiscountedPriceInCartItems(Long userId, int pointsToDeduct);

    void updateCartValue(Long userId, int revertedPoints);
    int getDiscountedPrice(Long userId);


    //validate isCartAmount is eligible for promotion or not
    void validatePromotionOnCart(Long userId);



}
