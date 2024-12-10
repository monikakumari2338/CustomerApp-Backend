package com.deepanshu.service;

import com.deepanshu.modal.*;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.StorePickupRepository;
import com.deepanshu.repository.StoreRepository;
import com.deepanshu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorePickupServiceImplementation implements StorePickupService {
	@Autowired
	private StorePickupRepository pickupRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StorePickupRepository storePickupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<Object[]> findPickupDateTimesByStoreId(Long storeId) {
		return pickupRepository.findAllByStoreId(storeId).stream()
				.map(pickup -> new Object[] { pickup.getPickupDateTime(), pickup.getStore().getName() })
				.collect(Collectors.toList());
	}

	@Override
	public void savePickupDateTime(Long storeId, List<Long> productIds, List<String> sizeNames,
			List<Integer> quantities, LocalDateTime pickupDateTime, String comment, Long userId) {
		Store store = storeRepository.findById(storeId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

		for (int i = 0; i < productIds.size(); i++) {
			Long productId = productIds.get(i);
			String sizeName = sizeNames.get(i);
			int quantity = quantities.get(i);

			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

			// Find the size
			ProductDetails selectedSize = product.getDetails().stream().filter(size -> size.getName().equals(sizeName))
					.findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid size"));

			// Update the inventory
			selectedSize.decreaseQuantity(quantity);

			// Save the updated product
			productRepository.save(product);

			StorePickup pickup = new StorePickup();
			pickup.setStore(store);
			pickup.setProduct(product);
			pickup.setSize(selectedSize);
			pickup.setPickupDateTime(pickupDateTime);
			pickup.setComment(comment);
			pickup.setUser(user);

			pickupRepository.save(pickup);
		}
	}

	@Override
	public StorePickup getActiveStorePickupForUser(User user) {
		List<StorePickup> activeStorePickups = storePickupRepository.findActiveStorePickupByUser(user);
		if (!activeStorePickups.isEmpty()) {
			return activeStorePickups.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updatePickupDateTime(Long pickupId, LocalDateTime newPickupDateTime, String newComment) {
		StorePickup storePickup = pickupRepository.findById(pickupId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid pickup ID"));

		storePickup.setPickupDateTime(newPickupDateTime);
		storePickup.setComment(newComment);
		pickupRepository.save(storePickup);
	}

	@Override
	public Optional<StorePickup> getStorePickupById(Long id) {
		return pickupRepository.findById(id);
	}
}
