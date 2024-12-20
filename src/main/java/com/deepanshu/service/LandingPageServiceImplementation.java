package com.deepanshu.service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepanshu.Dto.LandingPageDto;
import com.deepanshu.modal.ContinueShoppingItems;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.ProductDetails;
import com.deepanshu.modal.User;
import com.deepanshu.repository.ContinueShoppingItemsRepository;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.UserRepository;

@Service
public class LandingPageServiceImplementation implements LandingPageService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ContinueShoppingItemsRepository ContinueShoppingRepository;

	@Override
	public String createContinueShopping(Long userId, Long productId, String sku) {

		User user = userRepository.findById(userId).get();
		Product product = productRepository.findById(productId).get();
		LocalDateTime createdAt = LocalDateTime.now();

		ContinueShoppingItems items = new ContinueShoppingItems(user, product, createdAt, sku);
		ContinueShoppingRepository.save(items);
		return "Products Saved Successfully";
	}

	@Override
	public List<LandingPageDto> getContinueShoppingProducts(Long userId) {

		User user = userRepository.findById(userId).get();

		List<LandingPageDto> products = new ArrayList<>();
		List<ContinueShoppingItems> item = ContinueShoppingRepository.findAllByUser(user);

		for (int i = 0; i < item.size(); i++) {
			List<ProductDetails> details = new ArrayList<>(item.get(i).getProduct().getDetails());
			products.add(new LandingPageDto(item.get(i).getProduct().getId(), details.get(0).getImageData(),
					item.get(i).getProduct().getBrand(), item.get(i).getProduct().getTitle(),
					item.get(i).getProduct().getPrice(), item.get(i).getProduct().getDiscountedPrice()));
		}
		return products;
	}

	@Override
	public List<LandingPageDto> getTopRatedPrpducts() {

		List<Product> FiveStarRatedItems = productRepository.findTop4ByCountUsersRatedProductFiveStars();
		List<Product> FourStarRatedItems = productRepository.findTop2ByCountUsersRatedProductFourStars();

		List<LandingPageDto> products = new ArrayList<>();

		for (int i = 0; i < FiveStarRatedItems.size(); i++) {
			List<ProductDetails> details = new ArrayList<>(FiveStarRatedItems.get(i).getDetails());
			products.add(new LandingPageDto(FiveStarRatedItems.get(i).getId(), details.get(0).getImageData(),
					FiveStarRatedItems.get(i).getBrand(), FiveStarRatedItems.get(i).getTitle(),
					FiveStarRatedItems.get(i).getPrice(), FiveStarRatedItems.get(i).getDiscountedPrice()));
		}

		for (int i = 0; i < FourStarRatedItems.size(); i++) {
			List<ProductDetails> details = new ArrayList<>(FourStarRatedItems.get(i).getDetails());
			products.add(new LandingPageDto(FourStarRatedItems.get(i).getId(), details.get(0).getImageData(),
					FourStarRatedItems.get(i).getBrand(), FourStarRatedItems.get(i).getTitle(),
					FourStarRatedItems.get(i).getPrice(), FourStarRatedItems.get(i).getDiscountedPrice()));
		}

		return products;
	}

	@Override
	public List<LandingPageDto> getFeaturedProducts() {

		List<LandingPageDto> featuredProducts = new ArrayList<>();

		List<Product> products = productRepository.findAll();

		for (int i = 0; i < products.size(); i++) {
			if (!products.get(i).getEligiblePromotions().isEmpty()) {

				List<ProductDetails> details = new ArrayList<>(products.get(i).getDetails());

				featuredProducts.add(new LandingPageDto(products.get(i).getId(), details.get(0).getImageData(),
						products.get(i).getBrand(), products.get(i).getTitle(), products.get(i).getPrice(),
						products.get(i).getDiscountedPrice()));
			}

		}
		return featuredProducts;
	}

}
