package com.deepanshu.service;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Cart;
import com.deepanshu.modal.User;
import com.deepanshu.modal.Wishlist;
import com.deepanshu.request.AddItemRequest;

public interface WishlistService {

    public Wishlist createWishlist(User user);

    public String addWishlistItem(Long userId, AddItemRequest req) throws ProductException;

    public Wishlist findUserWishlist(Long userId);

}