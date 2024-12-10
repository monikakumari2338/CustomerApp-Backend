package com.deepanshu.controller;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.modal.Wishlist;
import com.deepanshu.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.ProductException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.User;
import com.deepanshu.request.AddItemRequest;
import com.deepanshu.response.ApiResponse;
import com.deepanshu.service.UserService;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "https://localhost:8081")
public class WishlistController {

    private WishlistService wishlistService;
    private UserService userService;

    public WishlistController(WishlistService wishlistService, UserService userService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<Wishlist> findUserWishlistHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Wishlist wishlist = wishlistService.findUserWishlist(user.getId());
        System.out.println("wishlist - " + wishlist.getUser().getEmail());
        return new ResponseEntity<Wishlist>(wishlist, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToWishlist(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        wishlistService.addWishlistItem(user.getId(), req);
        ApiResponse res = new ApiResponse("Item Added To Wishlist Successfully", true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);

    }


}