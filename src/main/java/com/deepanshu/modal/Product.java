package com.deepanshu.modal;

import java.time.LocalDateTime;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private int price;

	@Column(name = "discounted_price")
	private int discountedPrice;

	@Column(name = "discount_persent")
	private int discountPercent;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "brand")
	private String brand;

	@Column(name = "color")
	private String color;

	@Embedded
	@ElementCollection
	@Column(name = "details")
	private Set<ProductDetails> details = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
	@Column(name = "image_url")
	private Set<String> imageUrl = new HashSet<>();

	@Column(name = "country")
	private String country;
	@Column(name = "wearType")
	private String wearType;
	@Column(name = "fabric")
	private String fabric;
	@Column(name = "sleeves")
	private String sleeves;
	@Column(name = "fit")
	private String fit;
	@Column(name = "materialCare")
	private String materialCare;
	@Column(name = "productCode")
	private String productCode;
	@Column(name = "seller")
	private String seller;
	@Column(name = "sellerInfo")
	private String sellerInfo;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();

	@Column(name = "num_ratings")
	private int numRatings;

	@ManyToOne()
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "ingredient")
	private String ingredient;

	@Column(name = "packaging")
	private String packaging;

	@Column(name = "milk_type")
	private String milktype;

	@Column(name = "generic_name")
	private String genericname;

	@Column(name = "country_of_origin")
	private String countryoforigin;

	@Column(name = "preservatives")
	private String preservatives;

	@Column(name = "consume_within")
	private String consumewithin;

	private LocalDateTime createdAt;

	private boolean eligibleForBogo;

	@ElementCollection
	@CollectionTable(name = "product_pincodes", joinColumns = @JoinColumn(name = "product_id"))
	@Column(name = "pincode")
	private List<String> pincode = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "productId", referencedColumnName = "id")
	private List<Rating> ratingList;

	// Star rating counts
	private int countUsersRatedProductOneStar = 0;
	private int countUsersRatedProductTwoStars = 0;
	private int countUsersRatedProductThreeStars = 0;
	private int countUsersRatedProductFourStars = 0;
	private int countUsersRatedProductFiveStars = 0;

	// Averages for individual star ratings (optional)
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private double averageRatingForOneStar = 0.0;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private double averageRatingForTwoStars = 0.0;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private double averageRatingForThreeStars = 0.0;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private double averageRatingForFourStars = 0.0;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private double averageRatingForFiveStars = 0.0;

	// set product avg rating
	private double productRating = 0.0;

	private boolean eligibleForPromotion;
	// promotional price of product
	private String promotionalDiscountedPrice = null;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "product_promotions", // Join table name
			joinColumns = @JoinColumn(name = "product_id"), // Product column in join table
			inverseJoinColumns = @JoinColumn(name = "promotion_id") // Promotion column in join table
	)
	private List<Promotion> eligiblePromotions = new ArrayList<>();

	// applied promotion in the cart
	@ElementCollection
	@Column(name = "promoCode")
	@MapKeyColumn(name = "applied_promotion")
	private Map<String, Double> appliedPromotion = new HashMap<>();

	public Product() {
	}

	public Map<String, Double> getAppliedPromotion() {
		return appliedPromotion;
	}

	public void setAppliedPromotion(Map<String, Double> appliedPromotion) {
		this.appliedPromotion = appliedPromotion;
	}

	public String getPromotionalDiscountedPrice() {
		return promotionalDiscountedPrice;
	}

	public void setPromotionalDiscountedPrice(String promotionalDiscountedPrice) {
		this.promotionalDiscountedPrice = promotionalDiscountedPrice;
	}

	public boolean isEligibleForPromotion() {
		return eligibleForPromotion;
	}

	public void setEligibleForPromotion(boolean eligibleForPromotion) {
		this.eligibleForPromotion = eligibleForPromotion;
	}

	public List<Promotion> getEligiblePromotions() {
		return eligiblePromotions;
	}

	public void setEligiblePromotions(List<Promotion> eligiblePromotions) {
		this.eligiblePromotions = eligiblePromotions;
	}

	public int getCountUsersRatedProductOneStar() {
		return countUsersRatedProductOneStar;
	}

	public void setCountUsersRatedProductOneStar(int countUsersRatedProductOneStar) {
		this.countUsersRatedProductOneStar = countUsersRatedProductOneStar;
	}

	public int getCountUsersRatedProductTwoStars() {
		return countUsersRatedProductTwoStars;
	}

	public void setCountUsersRatedProductTwoStars(int countUsersRatedProductTwoStars) {
		this.countUsersRatedProductTwoStars = countUsersRatedProductTwoStars;
	}

	public int getCountUsersRatedProductThreeStars() {
		return countUsersRatedProductThreeStars;
	}

	public void setCountUsersRatedProductThreeStars(int countUsersRatedProductThreeStars) {
		this.countUsersRatedProductThreeStars = countUsersRatedProductThreeStars;
	}

	public int getCountUsersRatedProductFourStars() {
		return countUsersRatedProductFourStars;
	}

	public void setCountUsersRatedProductFourStars(int countUsersRatedProductFourStars) {
		this.countUsersRatedProductFourStars = countUsersRatedProductFourStars;
	}

	public int getCountUsersRatedProductFiveStars() {
		return countUsersRatedProductFiveStars;
	}

	public void setCountUsersRatedProductFiveStars(int countUsersRatedProductFiveStars) {
		this.countUsersRatedProductFiveStars = countUsersRatedProductFiveStars;
	}

	public double getAverageRatingForOneStar() {
		return averageRatingForOneStar;
	}

	public void setAverageRatingForOneStar(double averageRatingForOneStar) {
		this.averageRatingForOneStar = averageRatingForOneStar;
	}

	public double getAverageRatingForTwoStars() {
		return averageRatingForTwoStars;
	}

	public void setAverageRatingForTwoStars(double averageRatingForTwoStars) {
		this.averageRatingForTwoStars = averageRatingForTwoStars;
	}

	public double getAverageRatingForThreeStars() {
		return averageRatingForThreeStars;
	}

	public void setAverageRatingForThreeStars(double averageRatingForThreeStars) {
		this.averageRatingForThreeStars = averageRatingForThreeStars;
	}

	public double getAverageRatingForFourStars() {
		return averageRatingForFourStars;
	}

	public void setAverageRatingForFourStars(double averageRatingForFourStars) {
		this.averageRatingForFourStars = averageRatingForFourStars;
	}

	public double getAverageRatingForFiveStars() {
		return averageRatingForFiveStars;
	}

	public void setAverageRatingForFiveStars(double averageRatingForFiveStars) {
		this.averageRatingForFiveStars = averageRatingForFiveStars;
	}

	public double getProductRating() {
		return productRating;
	}

	public void setProductRating(double productRating) {
		this.productRating = productRating;
	}

	public List<Rating> getRatingList() {
		return ratingList;
	}

	public void setRatingList(List<Rating> ratingList) {
		this.ratingList = ratingList;
	}

	public List<String> getPincode() {
		return pincode;
	}

	public void setPincode(List<String> pincode) {
		this.pincode = pincode;
	}

	public String getSellerInfo() {
		return sellerInfo;
	}

	public void setSellerInfo(String sellerInfo) {
		this.sellerInfo = sellerInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Set<ProductDetails> getDetails() {
		return details;
	}

	public void setDetails(Set<ProductDetails> details) {
		this.details = details;
	}

	public Set<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(Set<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWearType() {
		return wearType;
	}

	public void setWearType(String wearType) {
		this.wearType = wearType;
	}

	public String getFabric() {
		return fabric;
	}

	public void setFabric(String fabric) {
		this.fabric = fabric;
	}

	public String getSleeves() {
		return sleeves;
	}

	public void setSleeves(String sleeves) {
		this.sleeves = sleeves;
	}

	public String getFit() {
		return fit;
	}

	public void setFit(String fit) {
		this.fit = fit;
	}

	public String getMaterialCare() {
		return materialCare;
	}

	public void setMaterialCare(String materialCare) {
		this.materialCare = materialCare;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public int getNumRatings() {
		return numRatings;
	}

	public void setNumRatings(int numRatings) {
		this.numRatings = numRatings;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getMilktype() {
		return milktype;
	}

	public void setMilktype(String milktype) {
		this.milktype = milktype;
	}

	public String getGenericname() {
		return genericname;
	}

	public void setGenericname(String genericname) {
		this.genericname = genericname;
	}

	public String getCountryoforigin() {
		return countryoforigin;
	}

	public void setCountryoforigin(String countryoforigin) {
		this.countryoforigin = countryoforigin;
	}

	public String getPreservatives() {
		return preservatives;
	}

	public void setPreservatives(String preservatives) {
		this.preservatives = preservatives;
	}

	public String getConsumewithin() {
		return consumewithin;
	}

	public void setConsumewithin(String consumewithin) {
		this.consumewithin = consumewithin;
	}

	public boolean isEligibleForBogo() {
		return eligibleForBogo;
	}

	public void setEligibleForBogo(boolean eligibleForBogo) {
		this.eligibleForBogo = eligibleForBogo;
	}

	public Product(String title, String description, int price, int discountedPrice, int discountPercent, int quantity,
			String brand, String color, Set<ProductDetails> details, Set<String> imageUrl, String country,
			String wearType, String fabric, String sleeves, String fit, String materialCare, String productCode,
			String seller, String sellerInfo, List<Review> reviews, int numRatings, Category category,
			String ingredient, String packaging, String milktype, String genericname, String countryoforigin,
			String preservatives, String consumewithin, LocalDateTime createdAt, boolean eligibleForBogo,
			List<String> pincode, List<Rating> ratingList, int countUsersRatedProductOneStar,
			int countUsersRatedProductTwoStars, int countUsersRatedProductThreeStars,
			int countUsersRatedProductFourStars, int countUsersRatedProductFiveStars, double averageRatingForOneStar,
			double averageRatingForTwoStars, double averageRatingForThreeStars, double averageRatingForFourStars,
			double averageRatingForFiveStars, double productRating, boolean eligibleForPromotion,
			String promotionalDiscountedPrice, List<Promotion> eligiblePromotions,
			Map<String, Double> appliedPromotion) {
		super();
		this.title = title;
		this.description = description;
		this.price = price;
		this.discountedPrice = discountedPrice;
		this.discountPercent = discountPercent;
		this.quantity = quantity;
		this.brand = brand;
		this.color = color;
		this.details = details;
		this.imageUrl = imageUrl;
		this.country = country;
		this.wearType = wearType;
		this.fabric = fabric;
		this.sleeves = sleeves;
		this.fit = fit;
		this.materialCare = materialCare;
		this.productCode = productCode;
		this.seller = seller;
		this.sellerInfo = sellerInfo;
		this.reviews = reviews;
		this.numRatings = numRatings;
		this.category = category;
		this.ingredient = ingredient;
		this.packaging = packaging;
		this.milktype = milktype;
		this.genericname = genericname;
		this.countryoforigin = countryoforigin;
		this.preservatives = preservatives;
		this.consumewithin = consumewithin;
		this.createdAt = createdAt;
		this.eligibleForBogo = eligibleForBogo;
		this.pincode = pincode;
		this.ratingList = ratingList;
		this.countUsersRatedProductOneStar = countUsersRatedProductOneStar;
		this.countUsersRatedProductTwoStars = countUsersRatedProductTwoStars;
		this.countUsersRatedProductThreeStars = countUsersRatedProductThreeStars;
		this.countUsersRatedProductFourStars = countUsersRatedProductFourStars;
		this.countUsersRatedProductFiveStars = countUsersRatedProductFiveStars;
		this.averageRatingForOneStar = averageRatingForOneStar;
		this.averageRatingForTwoStars = averageRatingForTwoStars;
		this.averageRatingForThreeStars = averageRatingForThreeStars;
		this.averageRatingForFourStars = averageRatingForFourStars;
		this.averageRatingForFiveStars = averageRatingForFiveStars;
		this.productRating = productRating;
		this.eligibleForPromotion = eligibleForPromotion;
		this.promotionalDiscountedPrice = promotionalDiscountedPrice;
		this.eligiblePromotions = eligiblePromotions;
		this.appliedPromotion = appliedPromotion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(brand, category, color, description, discountPercent, discountedPrice, id, imageUrl,
				numRatings, price, quantity, reviews, details, title, country, wearType, fabric, sleeves, fit,
				materialCare, productCode, seller);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(brand, other.brand) && Objects.equals(category, other.category)
				&& Objects.equals(color, other.color) && Objects.equals(description, other.description)
				&& discountPercent == other.discountPercent && discountedPrice == other.discountedPrice
				&& Objects.equals(id, other.id) && Objects.equals(imageUrl, other.imageUrl)
				&& numRatings == other.numRatings && price == other.price && quantity == other.quantity
				&& Objects.equals(reviews, other.reviews) && Objects.equals(details, other.details)
				&& Objects.equals(title, other.title);
	}

	public void increaseInventory(String sizeName, int quantity) {
		ProductDetails size = findSizeByName(sizeName);
		if (size != null) {
			size.increaseQuantity(quantity);
		} else {
			throw new IllegalArgumentException("Size " + sizeName + " does not exist for this product");
		}
	}

	public void decreaseInventory(String sizeName, int quantity) {
		ProductDetails size = findSizeByName(sizeName);
		if (size != null) {
			size.decreaseQuantity(quantity);
		} else {
			throw new IllegalArgumentException("Size " + sizeName + " does not exist for this product");
		}
	}

	private ProductDetails findSizeByName(String sizeName) {
		for (ProductDetails size : details) {
			if (size.getName().equals(sizeName)) {
				return size;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", details=" + details + "]";
	}

}
