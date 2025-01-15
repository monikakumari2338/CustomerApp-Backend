package com.deepanshu.controller;

import com.deepanshu.exception.WishlistItemException;
import com.deepanshu.modal.WishlistItem;
import com.deepanshu.service.WishlistItemService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.CartItem;
import com.deepanshu.modal.User;
import com.deepanshu.response.ApiResponse;
import com.deepanshu.service.CartItemService;
import com.deepanshu.service.UserService;

@RestController
@RequestMapping("/api/wishlist_items")
@CrossOrigin(origins = "https://localhost:8081")
@SecurityRequirement(name = "bearerAuth")
public class WishlistItemController {

    private WishlistItemService wishlistItemService;
    private UserService userService;

    public WishlistItemController(WishlistItemService wishlistItemService, UserService userService) {
        this.wishlistItemService = wishlistItemService;
        this.userService = userService;
    }

    @DeleteMapping("/{wishlistItemId}")
    public ResponseEntity<ApiResponse> deleteWishlistItemHandler(@PathVariable Long wishlistItemId, @RequestHeader("Authorization") String jwt) throws WishlistItemException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        wishlistItemService.removeWishlistItem(user.getId(), wishlistItemId);
        ApiResponse res = new ApiResponse("Item Removed From Wishlist", true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{wishlistItemId}")
    public ResponseEntity<WishlistItem> updateWishlistItemHandler(@PathVariable Long wishlistItemId, @RequestBody WishlistItem wishlistItem, @RequestHeader("Authorization") String jwt) throws WishlistItemException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        WishlistItem updatedWishlistItem = wishlistItemService.updateWishlistItem(user.getId(), wishlistItemId, wishlistItem);
        return new ResponseEntity<>(updatedWishlistItem, HttpStatus.ACCEPTED);
    }
}