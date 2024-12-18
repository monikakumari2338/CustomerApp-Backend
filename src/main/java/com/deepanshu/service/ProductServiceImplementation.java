package com.deepanshu.service;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.deepanshu.Dto.LandingPageDto;
import com.deepanshu.Dto.PdpDto;
import com.deepanshu.Dto.PdpDto.RatingDto;
import com.deepanshu.Dto.PdpDto.ReviewsDto;
import com.deepanshu.Dto.PdpDto.DetailDto;
import com.deepanshu.Dto.PdpDto.VariantInfoDto;
import com.deepanshu.Dto.PlpCardDto;
import com.deepanshu.config.ElasticSearchQuery;
import com.deepanshu.modal.*;
import com.deepanshu.repository.PromotionRepository;
import com.deepanshu.repository.RatingRepository;
import com.deepanshu.repository.ReviewRepository;
import com.deepanshu.request.PromotionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deepanshu.exception.ProductException;
import com.deepanshu.repository.CategoryRepository;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.request.CreateProductRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImplementation implements ProductService {

	private ProductRepository productRepository;
	private UserService userService;
	private CategoryRepository categoryRepository;

	@Autowired
	private ElasticSearchQuery elasticSearchQuery;
	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private RatingRepository ratingRepository;

	public ProductServiceImplementation(ProductRepository productRepository, UserService userService,
			CategoryRepository categoryRepository, ElasticSearchQuery elasticSearchQuery) {
		this.productRepository = productRepository;
		this.userService = userService;
		this.categoryRepository = categoryRepository;
		this.elasticSearchQuery = elasticSearchQuery;
	}

	@Override
	public Product createProduct(CreateProductRequest req) throws IOException {

		Product product = new Product();

		Category topLevel = categoryRepository.findByName(req.getTopLavelCategory());

		if (topLevel == null) {
			Category topLavelCategory = new Category();
			topLavelCategory.setName(req.getTopLavelCategory());
			topLavelCategory.setLevel(1);
			topLevel = categoryRepository.save(topLavelCategory);
		}

		Category secondLevel = categoryRepository.findByNameAndParant(req.getSecondLavelCategory(), topLevel.getName());
		if (secondLevel == null) {
			Category secondLavelCategory = new Category();
			secondLavelCategory.setName(req.getSecondLavelCategory());
			secondLavelCategory.setParentCategory(topLevel);
			secondLavelCategory.setLevel(2);
			secondLevel = categoryRepository.save(secondLavelCategory);
		}

		Category thirdLevel = categoryRepository.findByNameAndParant(req.getThirdLavelCategory(),
				secondLevel.getName());
		if (thirdLevel == null) {
			Category thirdLavelCategory = new Category();
			thirdLavelCategory.setName(req.getThirdLavelCategory());
			thirdLavelCategory.setParentCategory(secondLevel);
			thirdLavelCategory.setLevel(3);
			thirdLevel = categoryRepository.save(thirdLavelCategory);
		}

		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setDescription(req.getDescription());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setDiscountPercent(req.getDiscountPercent());
		product.setImageUrl(req.getImageUrl());
		product.setBrand(req.getBrand());
		product.setPrice(req.getPrice());
		product.setDetails(req.getProductDetails());
		product.setQuantity(req.getQuantity());
		product.setCategory(thirdLevel);
		product.setCountry(req.getCountry());
		product.setWearType(req.getWearType());
		product.setFabric(req.getFabric());
		product.setSleeves(req.getSleeves());
		product.setFit(req.getFit());
		product.setMaterialCare(req.getMaterialCare());
		product.setProductCode(req.getProductCode());
		product.setSeller(req.getSeller());

		product.setCreatedAt(LocalDateTime.now());
		product.setIngredient(req.getIngredient());
		product.setPackaging(req.getPackaging());
		product.setMilktype(req.getMilktype());
		product.setGenericname(req.getGenericname());

//      product.setCountryoforigin(req.getCountryoforigin());
		product.setPreservatives(req.getPreservatives());
		product.setConsumewithin(req.getConsumewithin());
		product.setEligibleForBogo(req.isEligibleForBogo());

		// Set pincodes
		product.setPincode(req.getPincode());

		System.out.println("products - " + product);
		// After saving the product to the relational database
		Product savedProduct = productRepository.save(product);

		// Optionally, add to Elasticsearch
		createProductListInElasticSearch(Arrays.asList(savedProduct));
		return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		System.out.println("delete product " + product.getId() + " - " + productId);
		product.getDetails().clear();
		productRepository.delete(product);
		return "Product deleted Successfully";
	}

	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
		Product product = findProductById(productId);
		if (req.getQuantity() != 0) {
			product.setQuantity(req.getQuantity());
		}
		if (req.getDescription() != null) {
			product.setDescription(req.getDescription());
		}
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = productRepository.findAll();

		return products;
	}

	@Override
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> opt = productRepository.findById(id);
		if (opt.isPresent()) {
			Product product = opt.get();

			return product;
		}
		throw new ProductException("product not found with this id " + id);
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		System.out.println("category --- " + category);
		List<Product> products = productRepository.findByCategory(category);
		return products;
	}

	@Override
	public List<Product> searchProduct(String query) {
		List<Product> products = productRepository.searchProduct(query);
		return products;
	}

	@Override
	public List<PlpCardDto> searchProductBySearchBar(String query) {
		List<Product> products = productRepository.searchProduct(query);
		List<PlpCardDto> plpCardDto = new ArrayList<>();

		products.forEach(item -> {
			List<ProductDetails> details = new ArrayList<>(item.getDetails());
			Set<String> colors = details.stream().map(ProductDetails::getColor).collect(Collectors.toSet());
			Set<String> sizes = details.stream().map(ProductDetails::getSize).collect(Collectors.toSet());
			// List<String> images =
			// details.stream().map(ProductDetails::getImageData).collect(Collectors.toList());

			System.out.println("details :" + details);
			List<String> images = new ArrayList<>();
			images.add(details.get(0).getImageData());
			images.add(details.get(0).getImageData());
			images.add(details.get(0).getImageData());

			double totalRatings = item.getCountUsersRatedProductFiveStars() + item.getCountUsersRatedProductFourStars()
					+ item.getAverageRatingForThreeStars() + item.getAverageRatingForTwoStars()
					+ item.getCountUsersRatedProductOneStar();
			double totalscore = 5 * item.getCountUsersRatedProductFiveStars()
					+ 4 * item.getCountUsersRatedProductFourStars() + 3 * item.getAverageRatingForThreeStars()
					+ 2 * item.getAverageRatingForTwoStars() + 1 * item.getCountUsersRatedProductOneStar();
			double avgRating = totalscore / totalRatings;

			plpCardDto.add(new PlpCardDto(item.getId(), images, item.getBrand(), item.getTitle(), item.getPrice(),
					item.getDiscountedPrice(), avgRating, colors, sizes));
		});

		return plpCardDto;
	}

	@Override
	public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize,
			String country, String wearType, String fabric, String sleeves, String fit, String materialCare,
			String productCode, String seller) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
		if (!colors.isEmpty()) {
			products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}

		if (stock != null) {
			if (stock.equals("in_stock")) {
				products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
			} else if (stock.equals("out_of_stock")) {
				products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
			}
		}
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

		List<Product> pageContent = products.subList(startIndex, endIndex);
		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
		return filteredProducts;
	}

	@Override
	public PdpDto getPdpProductById(Long newProductId) {

		Product product = productRepository.findById(newProductId).orElse(null);
		List<ProductDetails> details = new ArrayList<>(product.getDetails());

		List<String> displayimages = new ArrayList<>();
		displayimages.add(details.get(0).getImageData());

		Set<String> colors = details.stream().map(ProductDetails::getColor).collect(Collectors.toSet());
		Set<String> sizes = details.stream().map(ProductDetails::getSize).collect(Collectors.toSet());
		VariantInfoDto VariantInfoDto = new VariantInfoDto(details.get(0).getSku(), colors, sizes);

		double totalRatings = product.getCountUsersRatedProductFiveStars()
				+ product.getCountUsersRatedProductFourStars() + product.getAverageRatingForThreeStars()
				+ product.getAverageRatingForTwoStars() + product.getCountUsersRatedProductOneStar();
		double totalscore = 5 * product.getCountUsersRatedProductFiveStars()
				+ 4 * product.getCountUsersRatedProductFourStars() + 3 * product.getAverageRatingForThreeStars()
				+ 2 * product.getAverageRatingForTwoStars() + 1 * product.getCountUsersRatedProductOneStar();
		double avgRating = totalscore / totalRatings;
		RatingDto ratingDto = new RatingDto(avgRating, totalRatings);

		List<Review> reviews = reviewRepository.getAllProductsReview(newProductId);
		System.out.println("reviews :" + reviews);

		List<ReviewsDto> reviewsDto = new ArrayList<>();
		reviews.stream().forEach(review -> {
			List<Rating> ratedUser = ratingRepository.findByUserIdAndProductId(review.getUser().getId(), newProductId);
			reviewsDto.add(new ReviewsDto(review.getUser().getFirstName(), ratedUser.get(0).getGivenRating(),
					review.getReview()));
		});

		DetailDto detailDto = new DetailDto(product.getDescription(), product.getFabric(), product.getFit(),
				product.getMaterialCare(), product.getSeller());

		List<LandingPageDto> similarItemDto = new ArrayList<>();
		Category productCategory = product.getCategory();
		List<Product> products = productRepository.findByCategory(productCategory.getName());
		products.stream()
				.forEach(item -> similarItemDto.add(new LandingPageDto(item.getId(), details.get(0).getImageData(),
						item.getBrand(), item.getTitle(), item.getPrice(), item.getDiscountedPrice())));

		PdpDto PdpDto = new PdpDto(displayimages, product.getBrand(), product.getTitle(), VariantInfoDto,
				product.getPrice(), product.getDiscountedPrice(), ratingDto, detailDto, reviewsDto, similarItemDto);

		return PdpDto;
	}

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	// filter the data based on the topLevelCategory selected
	@Override
	public List<PlpCardDto> getTopCategoryWise(String category) {
		List<Product> allProducts = getAllProducts();

		List<Product> topCategoryProducts = allProducts.stream()
				.filter(product -> product.getCategory() != null && product.getCategory().getParentCategory() != null
						&& product.getCategory().getParentCategory().getName() != null && product.getCategory()
								.getParentCategory().getParentCategory().getName().equalsIgnoreCase(category))
				.collect(Collectors.toList());

		List<PlpCardDto> plpCardDto = new ArrayList<>();

		topCategoryProducts.forEach(item -> {
			List<ProductDetails> details = new ArrayList<>(item.getDetails());
			Set<String> colors = details.stream().map(ProductDetails::getColor).collect(Collectors.toSet());
			Set<String> sizes = details.stream().map(ProductDetails::getSize).collect(Collectors.toSet());
			// List<String> images =
			// details.stream().map(ProductDetails::getImageData).collect(Collectors.toList());
			System.out.println("details :" + details);
			List<String> images = new ArrayList<>();
			images.add(details.get(0).getImageData());
			images.add(details.get(0).getImageData());
			images.add(details.get(0).getImageData());

			double totalRatings = item.getCountUsersRatedProductFiveStars() + item.getCountUsersRatedProductFourStars()
					+ item.getAverageRatingForThreeStars() + item.getAverageRatingForTwoStars()
					+ item.getCountUsersRatedProductOneStar();
			double totalscore = 5 * item.getCountUsersRatedProductFiveStars()
					+ 4 * item.getCountUsersRatedProductFourStars() + 3 * item.getAverageRatingForThreeStars()
					+ 2 * item.getAverageRatingForTwoStars() + 1 * item.getCountUsersRatedProductOneStar();
			double avgRating = totalscore / totalRatings;

			plpCardDto.add(new PlpCardDto(item.getId(), images, item.getBrand(), item.getTitle(), item.getPrice(),
					item.getDiscountedPrice(), avgRating, colors, sizes));
		});
		return plpCardDto;
	}

	// filter the data based on the secondLevelCategory selected
	@Override
	public List<Product> getSecondCategoryWise(String category) {
		List<Product> allProducts = getAllProducts();
		return allProducts.stream()
				.filter(product -> product.getCategory().getParentCategory().getName().equalsIgnoreCase(category))
				.collect(Collectors.toList());
	}

	// filter the data based on the thirdLevelCategory selected
	@Override
	public List<Product> getThirdCategoryWise(String category) {
		List<Product> allProducts = getAllProducts();
		return allProducts.stream()
				.filter(product -> product.getCategory() != null && product.getCategory().getName() != null
						&& product.getCategory().getName().equalsIgnoreCase(category))
				.collect(Collectors.toList());
	}

	// sort the product based on price low to high
	@Override
	public List<Product> sortProductBasedOnLowPrice(List<Product> allProducts) {
		allProducts.sort(Comparator.comparingInt((Product product) -> product.getDiscountedPrice()));
		return allProducts;
	}

	// sort the product based on price high to low
	@Override
	public List<Product> sortProductBasedOnHighPrice(List<Product> allProducts) {

		allProducts.sort(Comparator.comparingInt((Product product) -> product.getDiscountedPrice()).reversed());
		return allProducts;
	}

	// sort the product based on high discount
	@Override
	public List<Product> sortProductBasedOnHighDiscount(List<Product> allProducts) {
		allProducts.sort(Comparator.comparingInt((Product product) -> product.getDiscountPercent()).reversed());
		return allProducts;
	}

	// sort the product based on new arrival
	@Override
	public List<Product> sortProductBasedOnNewArrival(List<Product> allProducts) {
		allProducts.sort(Comparator.comparing(product -> product.getCreatedAt()));
		return allProducts;
	}

	// sort the product based on rating
	@Override
	public List<Product> sortProductBasedOnRating(List<Product> allProducts) {
		allProducts.sort(Comparator.comparing(Product::getProductRating).reversed());
		return allProducts;
	}

	// fetch product brandName list,colors list,price range,sellerName List based on
	// filterName
	@Override
	public List<String> filterOnPLP(List<Product> products, String category) {
		if (products.isEmpty()) {
			return null;
		}
		List<String> filteredData = new ArrayList<>();
		Set<String> uniqueData = new HashSet<>();
		for (Product product : products) {
			if (category.equalsIgnoreCase("brand") && uniqueData.add(product.getBrand())) {
				filteredData.add(product.getBrand());
			} else if (category.equalsIgnoreCase("color") && uniqueData.add(product.getColor())) {
				filteredData.add(product.getColor());
			} else if (category.equalsIgnoreCase("seller") && uniqueData.add(product.getSeller())) {
				filteredData.add(product.getSeller());
			} else if (category.equalsIgnoreCase("price")) {
				// Use a Set to collect unique prices
				Set<Integer> uniquePrices = products.stream().map(Product::getDiscountedPrice)
						.collect(Collectors.toSet());

				// Find min and max prices from the unique prices
				if (!uniquePrices.isEmpty()) {
					int minPrice = Collections.min(uniquePrices);
					int maxPrice = Collections.max(uniquePrices);

					// Only add unique min and max prices to filteredData
					if (!filteredData.contains(String.valueOf(minPrice))) {
						filteredData.add(String.valueOf(minPrice));
					}
					if (!filteredData.contains(String.valueOf(maxPrice))) {
						filteredData.add(String.valueOf(maxPrice));
					}
				}
			}
		}
		return filteredData;
	}

	// filter products based on multiple filters
	@Override
	public List<Product> filterProductBasedOnMultiFilter(String category, List<String> brand, List<String> color,
			List<String> seller) {

		// get the current data on the Product Listing Page
		List<Product> allProducts = getThirdCategoryWise(category);

		// filter based on brandNames
		if (brand != null && !brand.isEmpty()) {
			allProducts = allProducts.stream().filter(product -> brand.contains(product.getBrand()))
					.collect(Collectors.toList());
		}

		// Filter by color
		if (color != null && !color.isEmpty()) {
			allProducts = allProducts.stream().filter(product -> color.contains(product.getColor()))
					.collect(Collectors.toList());
		}

		// Filter by seller
		if (seller != null && !seller.isEmpty()) {
			allProducts = allProducts.stream().filter(product -> seller.contains(product.getSeller()))
					.collect(Collectors.toList());
		}
		return allProducts;
	}

	// filter product based on brand
	@Override
	public List<Product> filterProductBasedOnBrand(String brand, String brandCategory) {
		// Get the products in the third-level category
		List<Product> allProducts = getThirdCategoryWise(brandCategory);

		// Filter products based on the brand
		List<Product> filteredProducts = allProducts.stream()
				.filter(product -> product.getBrand().equalsIgnoreCase(brand)).collect(Collectors.toList());

		return filteredProducts;
	}

	// search all pincode by productId
	@Override
	public List<String> getAllPincodeByProductId(Long productId) throws Exception {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new Exception("product not found with givenId:" + productId));
		return product.getPincode();
	}

	// search product availability by pincode
	@Override
	public Boolean searchProductAvailabiltyByPincode(Long productId, String pincode) throws Exception {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new Exception("product not found with givenId:" + productId));
		List<String> allPincode = product.getPincode();
		return allPincode.contains(pincode);
	}

	// Add product data in Elasticsearch
	@Transactional
	public void createProductListInElasticSearch(List<Product> products) throws IOException {
		// Map each Product to ProductElasticSearch using the mapper
//        List<ProductElasticSearch> elasticSearchProducts = products.stream()
//                .map(ProductMapper::toElasticSearch)  // Use the updated ProductMapper
//                .collect(Collectors.toList());
//
//        // For each mapped ProductElasticSearch, create or update the document in Elasticsearch
//        for (ProductElasticSearch product : elasticSearchProducts) {
//            elasticSearchQuery.createOrUpdateDocument(product);
//        }
	}

	// apply BOGO
	@Override
	public String applyBogo(PromotionRequest promotionRequest) {
		List<Product> productsToUpdate = new ArrayList<>();
		String productCategory = promotionRequest.getProductCategory();
		Promotion promotion = promotionRepository.findByPromotionCode("BOGO")
				.orElseThrow(() -> new RuntimeException("Promotion not found!"));

		for (Product product : productRepository.findAll()) {
			if (promotionRequest.getNonEligibleProductId() != null
					&& !promotionRequest.getNonEligibleProductId().contains(product.getId())
					&& promotionRequest.getEligibleProductId() != null
					&& promotionRequest.getEligibleProductId().contains(product.getId())
					&& !product.isEligibleForBogo()) {
				product.setEligibleForBogo(true);
				List<Promotion> eligiblePromotion = product.getEligiblePromotions();
				if (eligiblePromotion == null) {
					eligiblePromotion = new ArrayList<>();
				}
				eligiblePromotion.add(promotion);
				product.setEligiblePromotions(eligiblePromotion);
				productsToUpdate.add(product);
			} else {
				product.setEligibleForBogo(false);
			}
			if (productCategory != null && (product.getCategory().getName().equalsIgnoreCase(productCategory)
					|| product.getCategory().getParentCategory().getName().equalsIgnoreCase(productCategory)
					|| product.getCategory().getParentCategory().getParentCategory().getName()
							.equalsIgnoreCase(productCategory))
					&& !product.isEligibleForBogo()) {
				product.setEligibleForBogo(true);
				productsToUpdate.add(product);
				List<Promotion> eligiblePromotion = product.getEligiblePromotions();
				if (eligiblePromotion == null) {
					eligiblePromotion = new ArrayList<>();
				}
				eligiblePromotion.add(promotion);
				product.setEligiblePromotions(eligiblePromotion);
				productsToUpdate.add(product);
			} else {
				product.setEligibleForBogo(false);
			}
		}

		productRepository.saveAll(productsToUpdate);
		return "Promotion applied to " + productsToUpdate.size() + " products!";
	}

	@Override
	public Product getProductById(Long newProductId) {

		return productRepository.findById(newProductId).get();
	}

}
