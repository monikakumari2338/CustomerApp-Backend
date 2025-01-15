package com.deepanshu.service;

import com.deepanshu.modal.*;
import com.deepanshu.repository.CartRepository;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepanshu.exception.ProductException;
import com.deepanshu.request.AddItemRequest;

@Service
public class WishlistServiceImplementation implements WishlistService {

	private WishlistRepository wishlistRepository;
	private WishlistItemService wishlistItemService;
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemServiceImplementation cartItemServiceImplementation;

	public WishlistServiceImplementation(WishlistRepository wishlistRepository, WishlistItemService wishlistItemService,
			ProductService productService) {
		this.wishlistRepository = wishlistRepository;
		this.wishlistItemService = wishlistItemService;
		this.productService = productService;
	}

	@Override
	public Wishlist createWishlist(User user) {

		Wishlist wishlist = new Wishlist();
		wishlist.setUser(user);
		Wishlist createdWishlist = wishlistRepository.save(wishlist);
		return createdWishlist;
	}

	public Wishlist findUserWishlist(Long userId) {
		Wishlist wishlist = wishlistRepository.findByUserId(userId);
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		for (WishlistItem wishlistsItem : wishlist.getWishlistItems()) {
			totalPrice += wishlistsItem.getPrice();
			totalDiscountedPrice += wishlistsItem.getDiscountedPrice();
			totalItem += wishlistsItem.getQuantity();
		}

		wishlist.setTotalPrice(totalPrice);
		wishlist.setTotalItem(wishlist.getWishlistItems().size());
		wishlist.setTotalDiscountedPrice(totalDiscountedPrice);
		wishlist.setDiscounte(totalPrice - totalDiscountedPrice);
		wishlist.setTotalItem(totalItem);

		return wishlistRepository.save(wishlist);

	}

	@Override
	public String addWishlistItem(User user, AddItemRequest req) throws ProductException {
		System.out.println("userId " + user);
		Wishlist wishlist = wishlistRepository.findByUser(user);
		// Product product = productService.fi(req.getProductId());
		Product productDetails = productRepository.findProductBySku(req.getSku());
		System.out.println("wishlist :" + wishlist);
		Product product = productRepository.findById(productDetails.getId()).get();

		WishlistItem isPresent = wishlistItemService.isWishlistItemExist(wishlist, req.getSku(), user.getId());

		if (isPresent == null) {
			WishlistItem wishlistItem = new WishlistItem();
			wishlistItem.setProduct(product);
			wishlistItem.setWishlist(wishlist);
			wishlistItem.setQuantity(req.getQuantity());
			wishlistItem.setUserId(user.getId());

			int price = req.getQuantity() * product.getDiscountedPrice();
			wishlistItem.setPrice(price);
			wishlistItem.setSku(req.getSku());

			WishlistItem createdWishlistItem = wishlistItemService.createWishlistItem(wishlistItem);
			wishlist.getWishlistItems().add(createdWishlistItem);

		}

		return "Item Added To Wishlist";
	}
}