package com.deepanshu.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.deepanshu.exception.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.deepanshu.exception.CartItemException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Cart;
import com.deepanshu.modal.CartItem;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.User;
import com.deepanshu.repository.CartItemRepository;
import com.deepanshu.repository.CartRepository;
import com.deepanshu.request.AddItemRequest;

@Service
public class CartItemServiceImplementation implements CartItemService {

	private CartItemRepository cartItemRepository;
	private UserService userService;
	@Autowired
	@Lazy
	private CartService cartService;
	@Autowired
	private CartRepository cartRepository;

	public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService,
			CartRepository cartRepository) {
		this.cartItemRepository = cartItemRepository;
		this.userService = userService;
		this.cartRepository = cartRepository;
	}

	@Override
	public CartItem createCartItem(CartItem cartItem) throws UserException {

		cartItem.setQuantity(cartItem.getQuantity());
		cartItem.setPrice(cartItem.getProduct().getPrice());
		cartItem.setCategory(cartItem.getCategory());
		cartItem.setColor(cartItem.getColor());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

		CartItem createdCartItem = cartItemRepository.save(cartItem);

		return createdCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long id, AddItemRequest req)
			throws CartItemException, UserException, ProductException {
		CartItem item = findCartItemById(id);
		User user = userService.findUserById(item.getUserId());

		if (user.getId().equals(userId)) {
			// Update only the provided fields
//            if (cartItem.getSize() != null && !cartItem.getSize().isEmpty()) {
//                item.setSize(cartItem.getSize());
//            }

//			if (cartItem.getQuantity() > 0) {
//				item.setQuantity(cartItem.getQuantity());
//			}
			if (req.getQuantity() > 0 && item.getSku().equals(req.getSku())) {
//				int existingQty = item.getQuantity();
//				int newCartQty = existingQty + 1;
				System.out.println("cartItem.getQuantity() " + req.getQuantity());
				item.setQuantity(req.getQuantity());
			}

			// Update price and discounted price based on the updated quantity
			item.setPrice(item.getQuantity() * item.getProduct().getPrice());
			item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
			applyPromotions(userId, item.getProduct(), item.getQuantity());

			// Apply any additional promotions
			cartService.applyBestPromotionOnCart(userId);

			return cartItemRepository.save(item);
		} else {
			throw new CartItemException("You cannot update another user's cart item");
		}
	}

	@Override
	public CartItem isCartItemExist(Cart cart, String sku, Long userId) {

		CartItem cartItem = cartItemRepository.isCartItemExist(cart, sku, userId);

		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException, ProductException {

		System.out.println("userId- " + userId + " cartItemId " + cartItemId);

		CartItem cartItem = findCartItemById(cartItemId);

		User user = userService.findUserById(cartItem.getUserId());
		User reqUser = userService.findUserById(userId);

		Map<String, Double> promoCodeList = new HashMap<>();
		promoCodeList = cartItem.getProduct().getAppliedPromotion();

		if (user.getId().equals(reqUser.getId())) {
			Cart cart = cartItem.getCart();
			Map<String, Double> cartPromoList = cart.getPromoCode();
			if (!promoCodeList.isEmpty()) {
				for (Map.Entry<String, Double> promo : promoCodeList.entrySet()) {
					String promoKey = promo.getKey();
					Double promoValue = promo.getValue();

					if (cartPromoList.isEmpty()) {
						cartPromoList = new HashMap<>();
					}

					if (!cartPromoList.isEmpty() && cart.getPromoCode().containsKey(promoKey)) {
						Double cartPromoValue = cart.getPromoCode().get(promoKey);

						if (cartPromoValue - promoValue < 0) {
							cartPromoList.remove(promoKey);
						} else {
							cartPromoList.put(promoKey, cartPromoValue - promoValue);
						}
					}
				}
			}

			cartItem.getProduct().setAppliedPromotion(promoCodeList);
			cart.setPromoCode(cartPromoList);
			cartItemRepository.deleteById(cartItem.getId());
//            cartService.applyFivePercentOFFTransactionWise(userId);
//            cartService.applyFiveHundredOFFTransactionWise(userId);
			cartService.applyBestPromotionOnCart(userId);
//            cartService.applyItemWiseTwoHundredOFFOnProduct(userId,false);
//            cartService.applyItemWiseTwentyPercentOFFOnProduct(userId,false);
//            cartService.applyItemWiseTwentyPercentOFFOnProduct(userId,false);
			updatePromotionsAfterRemoval(cart, userId);
		} else {
			throw new UserException("you cannot remove another users item");
		}

	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt = cartItemRepository.findById(cartItemId);

		if (opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("cartItem not found with this id : " + cartItemId);
	}

	@Override
	public void updateDiscountedPriceInCartItems(Long userId, int pointsToDeduct) {
		// Retrieve cart items for the user
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
		for (CartItem cartItem : cartItems) {
			int currentDiscountedPrice = cartItem.getDiscountedPrice();
			int newDiscountedPrice = Math.max(0, currentDiscountedPrice - pointsToDeduct);
			cartItem.setDiscountedPrice(newDiscountedPrice);
			cartItemRepository.save(cartItem);
		}
	}

	@Override
	public void updateCartValue(Long userId, int revertedPoints) {

		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
		for (CartItem cartItem : cartItems) {
			int originalPrice = cartItem.getDiscountedPrice();
			int updatedPrice = originalPrice + revertedPoints;
			cartItem.setDiscountedPrice(updatedPrice);
			cartItemRepository.save(cartItem);
		}
	}

	@Override
	public int getDiscountedPrice(Long userId) {
		List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
		int totalDiscountedPrice = 0;
		for (CartItem cartItem : cartItems) {
			totalDiscountedPrice += cartItem.getDiscountedPrice();
		}
		return totalDiscountedPrice;
	}

	private void applyPromotions(Long userId, Product product, int quantity) throws ProductException {
		// Apply BOGO promotion if the product is eligible for BOGO
		if (product.isEligibleForBogo()) {
			try {
				cartService.applyBogoPromotion(userId);
			} catch (ProductException e) {
				throw new ProductException("Error applying BOGO promotion: " + e.getMessage());
			}
		} else {
			// Apply the best item-wise and transaction-wise promotions only if the product
			// is not eligible for BOGO
			try {
				cartService.applyBestTransactionWisePromotion(userId);
//                cartService.applyBestItemWisePromotion(userId);
			} catch (ProductException e) {
				throw new ProductException("Error applying promotions: " + e.getMessage());
			}
		}
	}

	private void updatePromotionsAfterRemoval(Cart cart, Long userId) throws ProductException {
		// Recalculate the promotion discount based on the current items in the cart
		if (cart.getPromotion_discount() == null) {
			cart.setPromotion_discount(BigDecimal.ZERO);
		}

		// Apply best promotions based on current cart items
		try {
			cartService.applyBestTransactionWisePromotion(userId);
			cartService.applyBestItemWisePromotion(userId);
		} catch (ProductException e) {
			throw new ProductException("Error applying promotions: " + e.getMessage());
		}

		// Save the updated cart after recalculating promotions
		cartRepository.save(cart);
	}

	// validate isCartAmount is eligible for promotion or not
	@Override
	public void validatePromotionOnCart(Long userId) {
		// get cart data
		Cart cart = cartRepository.findByUserId(userId);
		if (cart != null) {

		}
	}

}
