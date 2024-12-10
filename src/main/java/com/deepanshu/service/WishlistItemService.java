package com.deepanshu.service;

import com.deepanshu.exception.UserException;
import com.deepanshu.exception.WishlistItemException;
import com.deepanshu.modal.*;

public interface WishlistItemService {

    public WishlistItem createWishlistItem(WishlistItem wishlistItem);

    public WishlistItem updateWishlistItem(Long userId, Long id, WishlistItem wishlistItem) throws WishlistItemException, UserException;

    public WishlistItem isWishlistItemExist(Wishlist wishlist, Product product, String size, Long userId);

    public void removeWishlistItem(Long userId, Long wishlistItemId) throws WishlistItemException, UserException;

    public WishlistItem findWishlistItemById(Long wishlistItemId) throws WishlistItemException;

}