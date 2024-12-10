package com.deepanshu.service;

import java.util.Optional;

import com.deepanshu.exception.WishlistItemException;
import com.deepanshu.modal.*;
import com.deepanshu.repository.WishlistItemRepository;
import com.deepanshu.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.exception.UserException;
import com.deepanshu.repository.CartItemRepository;
import com.deepanshu.repository.CartRepository;

@Service
public class WishlistItemServiceImplementation implements WishlistItemService {

    private WishlistItemRepository wishlistItemRepository;
    private UserService userService;
    private WishlistRepository wishlistRepository;

    public WishlistItemServiceImplementation(WishlistItemRepository wishlistItemRepository, UserService userService) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.userService = userService;
    }

    @Override
    public WishlistItem createWishlistItem(WishlistItem wishlistItem) {

        wishlistItem.setQuantity(wishlistItem.getQuantity());
        wishlistItem.setPrice(wishlistItem.getProduct().getPrice() * wishlistItem.getQuantity());
        wishlistItem.setDiscountedPrice(wishlistItem.getProduct().getDiscountedPrice() * wishlistItem.getQuantity());

        WishlistItem createdWishlistItem = wishlistItemRepository.save(wishlistItem);

        return createdWishlistItem;
    }

    @Override
    public WishlistItem updateWishlistItem(Long userId, Long id, WishlistItem wishlistItem) throws WishlistItemException, UserException {

        WishlistItem item = findWishlistItemById(id);
        User user = userService.findUserById(item.getUserId());

        if (user.getId().equals(userId)) {
            item.setQuantity(wishlistItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
            return wishlistItemRepository.save(item);
        } else {
            throw new WishlistItemException("You cannot update  another users cart_item");
        }
    }

    @Override
    public WishlistItem isWishlistItemExist(Wishlist wishlist, Product product, String size, Long userId) {
        WishlistItem wishlistItem = wishlistItemRepository.isWishlistItemExist(wishlist, product, size, userId);
        return wishlistItem;
    }


    @Override
    public void removeWishlistItem(Long userId, Long wishlistItemId) throws WishlistItemException, UserException {
        System.out.println("userId- " + userId + " wishlistItemId " + wishlistItemId);
        WishlistItem wishlistItem = findWishlistItemById(wishlistItemId);
        User user = userService.findUserById(wishlistItem.getUserId());
        User reqUser = userService.findUserById(userId);
        if (user.getId().equals(reqUser.getId())) {
            wishlistItemRepository.deleteById(wishlistItem.getId());
        } else {
            throw new UserException("you cannot remove another users item");
        }
    }

    @Override
    public WishlistItem findWishlistItemById(Long wishlistItemId) throws WishlistItemException {
        Optional<WishlistItem> opt = wishlistItemRepository.findById(wishlistItemId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new WishlistItemException("wishlistItem not found with this id : " + wishlistItemId);
    }

}