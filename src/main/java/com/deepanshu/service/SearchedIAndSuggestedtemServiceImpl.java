package com.deepanshu.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepanshu.modal.Order;
import com.deepanshu.modal.OrderItem;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.SearchedItem;
import com.deepanshu.modal.User;
import com.deepanshu.modal.WishlistItem;
import com.deepanshu.repository.OrderItemRepository;
import com.deepanshu.repository.OrderRepository;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.SearchedItemRepository;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.repository.WishlistItemRepository;

@Service
public class SearchedIAndSuggestedtemServiceImpl implements SearchedIAndSuggestedtemService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SearchedItemRepository searchedItemRepository;

	@Autowired
	private WishlistItemRepository wishlistItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public String saveSearchedItems(String param, Long userId) {

		LocalDateTime dateTime = LocalDateTime.now();
		List<Product> products = productRepository.findByDescriptionContaining(param);
		User user = userRepository.findById(userId).get();
		products.forEach(product -> {
			SearchedItem searchedItem = new SearchedItem(user, product, dateTime);
			searchedItemRepository.save(searchedItem);
		});
		return "Searched items save successfully";
	}

	private Product findProductById(Long productId) {
		// Replace this with a repository call to fetch the Product by ID
		return productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));
	}

	public List<Product> getSuggestedProducts(Long userId) {
		// Priority values
		int searchPriority = 5;
		int wishlistPriority = 10;
		int orderedPriority = 15;

		User user = userRepository.findById(userId).get();

		List<SearchedItem> searchedItems = searchedItemRepository.findTop5ByUserOrderByDateTimeDesc(user);
		List<WishlistItem> wishListItems = wishlistItemRepository.findAllByUserId(userId);

		List<Order> latestOrders = orderRepository.findTop5ByUserOrderByCreatedAtDesc(user);
		List<Long> orderIds = latestOrders.stream().map(Order::getId).collect(Collectors.toList());
		List<OrderItem> orderedProducts = orderItemRepository.findAllByOrderIds(orderIds);

		Map<Long, Integer> priorityScores = new HashMap<>();

		for (OrderItem productId : orderedProducts) {
			priorityScores.put(productId.getProduct().getId(),
					priorityScores.getOrDefault(productId.getProduct().getId(), 0) + orderedPriority);
		}
		// Process the "wishlist" list
		for (WishlistItem productId : wishListItems) {
			priorityScores.put(productId.getProduct().getId(),
					priorityScores.getOrDefault(productId.getProduct().getId(), 0) + wishlistPriority);
		}
		// Process the "searched" list
		for (SearchedItem productId : searchedItems) {
			priorityScores.put(productId.getProduct().getId(),
					priorityScores.getOrDefault(productId.getProduct().getId(), 0) + searchPriority);
		}

		System.out.println("priorityScores  " + priorityScores);
		return priorityScores.entrySet().stream().sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
				.limit(10).map(entry -> findProductById(entry.getKey())).collect(Collectors.toList());
	}

}
