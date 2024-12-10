package com.deepanshu.service;

import com.deepanshu.modal.*;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.PromotionRepository;
import com.deepanshu.request.PromotionRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImplementation implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Autowired
	private ProductRepository productRepository;

	// Create or update a promotion
	@Override
	@Transactional
	public Promotion createPromotion(PromotionDTO promotionDTO) {
		// Check if promotion already exists
		Promotion promotion = promotionRepository.findByPromotionCode(promotionDTO.getPromotionCode()).orElseGet(() -> {
			Promotion newPromotion = new Promotion();
			newPromotion.setCreatedAt(LocalDateTime.now());
			newPromotion.setPromotionStartdate(LocalDateTime.now());
			return newPromotion;
		});

		// Set promotion details
		promotion.setPromotionName(promotionDTO.getPromotionName());
		promotion.setPromotionDescription(promotionDTO.getPromotionDescription());
		promotion.setPromotionCode(promotionDTO.getPromotionCode());
		promotion.setPromotionType(promotionDTO.getPromotionType());
		promotion.setDiscountValue(promotionDTO.getDiscountValue());

		// Set updated and end date
		promotion.setUpdatedAt(LocalDateTime.now());
		LocalDate endDate = LocalDate.parse(promotionDTO.getPromotionEndDate(),
				DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		promotion.setPromotionEndDate(endDate.atTime(23, 59, 59)); // Set time to 23:59:59

		// Set other promotion details
		promotion.setMinOrderValue(promotionDTO.getPromotionMinOrderValue());
		promotion.setMaxOrderValue(promotionDTO.getPromotionMaxOrderValue());
		promotion.setMaxDiscountOnCart(promotionDTO.getMaxDiscountOnCart());
		promotion.setUsageLimit(promotionDTO.getPromotionUsageLimit());
		promotion.setUsageCount(promotionDTO.getPromotionUsageCount());
		promotion.setPromotionActive(promotionDTO.isPromotionActive());
		promotion.setTemporarilyInactive(promotionDTO.isTemporarilyInactive());
		promotion.setAppliedCategory(promotionDTO.getAppliedCategory());
		if (promotionDTO.getExcludedFromPromotionList() != null) {
			promotion.setExcludedFromPromotionList(promotionDTO.getExcludedFromPromotionList());
			promotion.getExcludedFromPromotionList().clear();
		}

//        // Update the product list with the promotion
//        Set<Long> applicableProductIds = getEligibleProductIdForPromotion(promotion);
//        updateProductListWithPromotion(applicableProductIds, promotion);

		return promotionRepository.save(promotion);
	}

	// Get all promotions
	@Override
	public List<Promotion> getAllPromotions() {
		List<Promotion> promotions = promotionRepository.findAll(); // Fetch all promotions from the repository
		return promotions;
	}

	// Get all product IDs that are eligible for the promotion
	public Set<Long> getEligibleProductIdForPromotion(Promotion promotion) {
		List<Product> productList = productRepository.findAll();
		Set<Long> applicableProductIds = new HashSet<>();

		// Store applied brand names for filtering
		List<String> appliedBrands = promotion.getAppliedCategory().stream()
				.map(category -> category.getBrandName().toLowerCase()).collect(Collectors.toList());

		// Filter based on brandName
		applicableProductIds
				.addAll(productList.stream().filter(product -> appliedBrands.contains(product.getBrand().toLowerCase()))
						.map(Product::getId).collect(Collectors.toList()));

		// Filter based on brand's grandparent category name
		applicableProductIds.addAll(productList.stream().filter(product -> {
			Category category = product.getCategory();
			return category.getParentCategory() != null && category.getParentCategory().getParentCategory() != null
					&& appliedBrands.contains(category.getParentCategory().getParentCategory().getName());
		}).map(Product::getId).collect(Collectors.toList()));

		// Further filtering based on second-level and third-level categories
		applicableProductIds.addAll(getProductIdsByCategoryLevel(productList, appliedBrands, 2));
		applicableProductIds.addAll(getProductIdsByCategoryLevel(productList, appliedBrands, 3));

		// Exclude specific product IDs which is in excluded productList
		applicableProductIds.removeAll(getExcludedProductIds(promotion));

		return applicableProductIds;
	}

	// Get product IDs based on category level (2nd or 3rd level)
	private List<Long> getProductIdsByCategoryLevel(List<Product> productList, List<String> appliedBrands, int level) {
		return productList.stream().filter(product -> {
			Category category = product.getCategory();
			if (level == 2) {
				return category.getParentCategory() != null
						&& appliedBrands.contains(category.getParentCategory().getName());
			} else if (level == 3) {
				return appliedBrands.contains(category.getName());
			}
			return false;
		}).map(Product::getId).collect(Collectors.toList());
	}

	// Get the list of excluded product IDs
	private Set<Long> getExcludedProductIds(Promotion promotion) {
		return promotion.getExcludedFromPromotionList().stream().map(Long::longValue) // Convert each Integer to Long
				.collect(Collectors.toSet());
	}

	// Update product list with the promotion
	void updateProductListWithPromotion(Set<Long> applicableProductIds, Promotion promotion) {
		System.out.println("Applicable Product IDs:");
		for (Long id : applicableProductIds) {
			Optional<Product> optionalProduct = productRepository.findById(id);
			if (optionalProduct.isPresent()) {
				Product product = optionalProduct.get();
				// Add the promotion to the product's eligible promotions list
				product.getEligiblePromotions().add(promotion);
				// Save the updated product

				System.out.println("Promotion added to product with ID: " + id);
			} else {
				System.out.println("Product with ID " + id + " not found.");
			}
		}
	}

	@Override
	// remove promotion byId
	public Promotion removePromotionById(Long promotionId) {
		Promotion promotion = promotionRepository.findById(promotionId)
				.orElseThrow(() -> new RuntimeException("Promotion not found with given id:" + promotionId));

		// remove promotion from the productList also when it removed from DB
		List<Product> productList = productRepository.findAll().stream()
				.filter(product -> product.getEligiblePromotions().contains(promotion)).collect(Collectors.toList());
		for (Product product : productList) {
			product.getEligiblePromotions().remove(promotion);
			productRepository.save(product);
		}

		promotionRepository.delete(promotion);
		return promotion;
	}

	// Helper method to check if a product is eligible for promotion by checking
	// category hierarchy
	private String isProductEligibleForPromotion(Product product, Map<String, Set<String>> brandCategoryMap) {
		String brandName = product.getBrand().toLowerCase();
		if (!brandCategoryMap.containsKey(brandName))
			return null;

		Set<String> targetCategories = brandCategoryMap.get(brandName);

		// Check for matching category
		String productCategoryName = product.getCategory().getName().toLowerCase();
		if (targetCategories.contains(productCategoryName)) {
			return productCategoryName; // Matched category found
		}

		// Check parent category
		if (product.getCategory().getParentCategory() != null) {
			String parentCategoryName = product.getCategory().getParentCategory().getName().toLowerCase();
			if (targetCategories.contains(parentCategoryName)) {
				return parentCategoryName; // Matched parent category found
			}

			// Check grandparent category
			if (product.getCategory().getParentCategory().getParentCategory() != null) {
				String grandparentCategoryName = product.getCategory().getParentCategory().getParentCategory().getName()
						.toLowerCase();
				if (targetCategories.contains(grandparentCategoryName)) {
					return grandparentCategoryName; // Matched grandparent category found
				}
			}
		}

		// Return null if no match is found
		return null;
	}

	// apply promotion on productId's
	@Override
	public String applyPromotionOnProductId(PromotionRequest promotionRequest) {
		String promotionCode = promotionRequest.getPromotionCode();
		// fetch promotion by promoCode
		Promotion promotion = promotionRepository.findByPromotionCode(promotionCode)
				.orElseThrow(() -> new RuntimeException("Promotion not exist with" + promotionCode));

		// store each product with their brand and brandCategory
		Map<String, Set<String>> mapBrandAndCategory = new HashMap<>();
		if (promotionRequest.getAppliedCategory() != null) {
			for (CategoryPair pair : promotionRequest.getAppliedCategory()) {
				mapBrandAndCategory.computeIfAbsent(pair.getBrandName().toLowerCase(), k -> new HashSet<>())
						.add(pair.getBrandCategory().toLowerCase());
			}
		}

		List<CategoryPair> categoryPairList = promotionRequest.getAppliedCategory();
		for (Product product : productRepository.findAll()) {

			// check is anyPromotionApplied
			boolean isAnyPromotionApplied = false;
			// if promotion applied by brandName, and category
			boolean isPromotionAlreadyExist = product.getEligiblePromotions().stream()
					.anyMatch(isPromotionApplied -> isPromotionApplied.getPromotionCode().contains(promotionCode));
			// check product match with which category
			String matchedCategory = isProductEligibleForPromotion(product, mapBrandAndCategory);
			System.out.println(
					"\nmatchedCategory:" + matchedCategory + " \nisPromotionAlreadyExist:" + isPromotionAlreadyExist);

			if (!isPromotionAlreadyExist) {
				if (categoryPairList != null) {
					if (matchedCategory != null) {
						if (promotionCode == "BOGO") {
							product.setEligibleForBogo(true);
						}
						isAnyPromotionApplied = true;
						if (!product.getEligiblePromotions().contains(promotion)) {
							product.getEligiblePromotions().add(promotion);
						}
						if (!promotion.getIncludedProductList().contains(product.getId())) {
							promotion.getIncludedProductList().add(product.getId());
						}
						// Create a new CategoryPair
						CategoryPair newCategoryPair = new CategoryPair(product.getBrand(), matchedCategory);

						// Check if the CategoryPair already exists in appliedCategory
						if (!promotion.getAppliedCategory().contains(newCategoryPair)) {
							promotion.getAppliedCategory().add(newCategoryPair);
						}
					}
				}
//            else{
//                product.getEligiblePromotions().remove(promotion);
//            }

				// if promotion applied by exclusionList
				if (promotionRequest.getEligibleProductId() == null
						&& promotionRequest.getNonEligibleProductId() != null
						&& !promotionRequest.getNonEligibleProductId().contains(product.getId())) {
					if (promotionCode.equals("BOGO")) {
						product.setEligibleForBogo(true);
					}
					isAnyPromotionApplied = true;
					if (!product.getEligiblePromotions().contains(promotion)) {
						product.getEligiblePromotions().add(promotion);
					}
					if (!promotion.getIncludedProductList().contains(product.getId())) {
						promotion.getIncludedProductList().add(product.getId());
						product.setEligibleForPromotion(true);
					}
					// Create a new CategoryPair
					CategoryPair newCategoryPair = new CategoryPair(product.getBrand(),
							matchedCategory != null ? matchedCategory : product.getCategory().getName());

					// Check if the CategoryPair already exists in appliedCategory
					if (!promotion.getAppliedCategory().contains(newCategoryPair)) {
						promotion.getAppliedCategory().add(newCategoryPair);
					}
				}

				// if promotion applied by inclusionList
				if (promotionRequest.getEligibleProductId() != null
						&& promotionRequest.getNonEligibleProductId() == null
						&& promotionRequest.getEligibleProductId().contains(product.getId())) {
					if (promotionCode.equals("BOGO")) {
						product.setEligibleForBogo(true);
					}
					isAnyPromotionApplied = true;
					if (!product.getEligiblePromotions().contains(promotion)) {
						product.getEligiblePromotions().add(promotion);
					}
					if (!promotion.getIncludedProductList().contains(product.getId())) {
						promotion.getIncludedProductList().add(product.getId());
					}
					// Create a new CategoryPair
					CategoryPair newCategoryPair = new CategoryPair(product.getBrand(),
							matchedCategory != null ? matchedCategory : product.getCategory().getName());

					// Check if the CategoryPair already exists in appliedCategory
					if (!promotion.getAppliedCategory().contains(newCategoryPair)) {
						promotion.getAppliedCategory().add(newCategoryPair);
					}
				}

				// if promotion applied by inclusionList and exclusion List
				if (promotionRequest.getEligibleProductId() != null
						&& promotionRequest.getNonEligibleProductId() != null) {
					if (promotionRequest.getEligibleProductId().contains(product.getId())
							&& !promotionRequest.getNonEligibleProductId().contains(product.getId())) {
						if (promotionCode.equals("BOGO")) {
							product.setEligibleForBogo(true);
						}
						isAnyPromotionApplied = true;
						if (!product.getEligiblePromotions().contains(promotion)) {
							product.getEligiblePromotions().add(promotion);
						}
						if (!promotion.getIncludedProductList().contains(product.getId())) {
							promotion.getIncludedProductList().add(product.getId());
						}
						// Create a new CategoryPair
						CategoryPair newCategoryPair = new CategoryPair(product.getBrand(),
								matchedCategory != null ? matchedCategory : product.getCategory().getName());

						// Check if the CategoryPair already exists in appliedCategory
						if (!promotion.getAppliedCategory().contains(newCategoryPair)) {
							promotion.getAppliedCategory().add(newCategoryPair);
						}
					}
				}

				// if promotion apply by productCategory
				if (promotionRequest.getProductCategory() != null
						&& (promotionRequest.getProductCategory().equalsIgnoreCase(product.getCategory().getName())
								|| promotionRequest.getProductCategory()
										.equalsIgnoreCase(product.getCategory().getParentCategory().getName())
								|| promotionRequest.getProductCategory().equalsIgnoreCase(
										product.getCategory().getParentCategory().getParentCategory().getName()))) {
					if (promotionCode.equals("BOGO")) {
						product.setEligibleForBogo(true);
					}
					isAnyPromotionApplied = true;
					if (!product.getEligiblePromotions().contains(promotion)) {
						product.getEligiblePromotions().add(promotion);
					}
					if (!promotion.getIncludedProductList().contains(product.getId())) {
						promotion.getIncludedProductList().add(product.getId());
					}
					// Create a new CategoryPair
					CategoryPair newCategoryPair = new CategoryPair(product.getBrand(),
							matchedCategory != null ? matchedCategory : product.getCategory().getName());

					// Check if the CategoryPair already exists in appliedCategory
					if (!promotion.getAppliedCategory().contains(newCategoryPair)) {
						promotion.getAppliedCategory().add(newCategoryPair);
					}
				}

//                if(!isAnyPromotionApplied){
//                    product.getEligiblePromotions().remove(promotixon);
//                    if (promotionCode == "BOGO") {
//                        product.setEligibleForBogo(false);
//                    }
//                }

			}

			productRepository.save(product);
		}

		return promotionRequest.getPromotionCode() + " promotion applied to products!";
	}

	// Remove promotion on productId's
	@Override
	public String removePromotionFromProductId(PromotionRequest promotionRequest) {
		String promotionCode = promotionRequest.getPromotionCode();

		// Fetch promotion by promoCode
		Promotion promotion = promotionRepository.findByPromotionCode(promotionCode)
				.orElseThrow(() -> new RuntimeException("Promotion not exist with " + promotionCode));

		// Store each product with their brand and brandCategory
		Map<String, Set<String>> mapBrandAndCategory = new HashMap<>();
		if (promotionRequest.getAppliedCategory() != null) {
			for (CategoryPair pair : promotionRequest.getAppliedCategory()) {
				mapBrandAndCategory.computeIfAbsent(pair.getBrandName().toLowerCase(), k -> new HashSet<>())
						.add(pair.getBrandCategory().toLowerCase());
			}
		}

		// If promotion applied by brandName and category
		List<CategoryPair> categoryPairList = promotionRequest.getAppliedCategory();
		for (Product product : productRepository.findAll()) {
			boolean isPromotionAlreadyExist = product.getEligiblePromotions().stream()
					.anyMatch(isPromotionApplied -> isPromotionApplied.getPromotionCode().contains(promotionCode));

			// Check which category the product matches
			String matchedCategory = isProductEligibleForPromotion(product, mapBrandAndCategory);
			System.out.println(
					"\nmatchedCategory: " + matchedCategory + " \nisPromotionAlreadyExist: " + isPromotionAlreadyExist);

			if (categoryPairList != null) {
				if (isPromotionAlreadyExist && matchedCategory != null) {
					if (promotionCode.equals("BOGO")) {
						product.setEligibleForBogo(false);
					}
					product.getEligiblePromotions().remove(promotion);
					promotion.getIncludedProductList().remove(product.getId());
					promotion.getAppliedCategory().removeIf(catPair ->
					// 1. Check if brandName is not null and brandCategory is null
					(catPair.getBrandName() != null && catPair.getBrandCategory() == null
							&& catPair.getBrandName().equalsIgnoreCase(product.getBrand())) ||

					// 2. Existing condition where both brandName and brandCategory are checked
							(catPair.getBrandName().equalsIgnoreCase(product.getBrand())
									&& (catPair.getBrandCategory() != null && catPair.getBrandCategory()
											.equalsIgnoreCase(product.getCategory().getName()))));

				}
			}

			// If promotion applied by exclusionList
			if (promotionRequest.getNonEligibleProductId() != null
					&& !promotionRequest.getNonEligibleProductId().contains(product.getId())) {
				if (promotionCode.equals("BOGO")) {
					product.setEligibleForBogo(false);
				}
				product.getEligiblePromotions().remove(promotion);
				promotion.getIncludedProductList().remove(product.getId());
				promotion.getAppliedCategory().removeIf(catPair ->
				// 1. Check if brandName is not null and brandCategory is null
				(catPair.getBrandName() != null && catPair.getBrandCategory() == null
						&& catPair.getBrandName().equalsIgnoreCase(product.getBrand())) ||

				// 2. Existing condition where both brandName and brandCategory are checked
						(catPair.getBrandName().equalsIgnoreCase(product.getBrand())
								&& (catPair.getBrandCategory() != null && catPair.getBrandCategory()
										.equalsIgnoreCase(product.getCategory().getName()))));

			}

			// If promotion applied by inclusionList
			if (promotionRequest.getEligibleProductId() != null
					&& promotionRequest.getEligibleProductId().contains(product.getId())) {
				if (promotionCode.equals("BOGO")) {
					product.setEligibleForBogo(false);
				}
				product.getEligiblePromotions().remove(promotion);
				promotion.getIncludedProductList().remove(product.getId());
				promotion.getAppliedCategory().removeIf(catPair ->
				// 1. Check if brandName is not null and brandCategory is null
				(catPair.getBrandName() != null && catPair.getBrandCategory() == null
						&& catPair.getBrandName().equalsIgnoreCase(product.getBrand())) ||

				// 2. Existing condition where both brandName and brandCategory are checked
						(catPair.getBrandName().equalsIgnoreCase(product.getBrand())
								&& (catPair.getBrandCategory() != null && catPair.getBrandCategory()
										.equalsIgnoreCase(product.getCategory().getName()))));

			}

			// If promotion applies by productCategory
			if (promotionRequest.getProductCategory() != null
					&& (promotionRequest.getProductCategory().equalsIgnoreCase(product.getCategory().getName())
							|| promotionRequest.getProductCategory()
									.equalsIgnoreCase(product.getCategory().getParentCategory().getName())
							|| promotionRequest.getProductCategory().equalsIgnoreCase(
									product.getCategory().getParentCategory().getParentCategory().getName()))) {
				if (promotionCode.equals("BOGO")) {
					product.setEligibleForBogo(false);
				}

				product.getEligiblePromotions().remove(promotion);
				promotion.getIncludedProductList().remove(product.getId());
				promotion.getAppliedCategory().removeIf(catPair ->
				// 1. Check if brandName is not null and brandCategory is null
				(catPair.getBrandName() != null && catPair.getBrandCategory() == null
						&& catPair.getBrandName().equalsIgnoreCase(product.getBrand())) ||

				// 2. Existing condition where both brandName and brandCategory are checked
						(catPair.getBrandName().equalsIgnoreCase(product.getBrand())
								&& (catPair.getBrandCategory() != null && catPair.getBrandCategory()
										.equalsIgnoreCase(product.getCategory().getName()))));

			}

			productRepository.save(product);
		}

		return promotionRequest.getPromotionCode() + " promotion removed from products!";
	}

}
