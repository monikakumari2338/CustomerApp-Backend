package com.deepanshu.controller;

import com.deepanshu.exception.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.CartItem;
import com.deepanshu.modal.User;
import com.deepanshu.request.AddItemRequest;
import com.deepanshu.response.ApiResponse;
import com.deepanshu.service.CartItemService;
import com.deepanshu.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/cart_items")
@CrossOrigin(origins = "https://localhost:8081")
@SecurityRequirement(name = "bearerAuth")
public class CartItemController {

	private CartItemService cartItemService;
	private UserService userService;

	public CartItemController(CartItemService cartItemService, UserService userService) {
		this.cartItemService = cartItemService;
		this.userService = userService;
	}

	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId,
			@RequestHeader("Authorization") String jwt) throws CartItemException, UserException, ProductException {

		User user = userService.findUserProfileByJwt(jwt);
		cartItemService.removeCartItem(user.getId(), cartItemId);

		ApiResponse res = new ApiResponse("Item Remove From Cart", true);

		return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
	}

	@PutMapping("/{cartItemId}")
	public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId,
			@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt)
			throws CartItemException, UserException, ProductException {

		User user = userService.findUserProfileByJwt(jwt);

		CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, req);

		return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
	}
}
