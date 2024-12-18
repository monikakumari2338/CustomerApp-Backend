package com.deepanshu.Dto;

import java.util.List;
import java.util.Set;

public class PdpDto {

	private List<String> displayImages;
	private String brand;
	private String product;
	private VariantInfoDto variantInfo;
	private int price;
	private int sellingPrice;
	private RatingDto rating;
	private DetailDto details;
	private List<ReviewsDto> reviews;
	private List<LandingPageDto> similarItems;

	// Getters and Setters
	public List<String> getDisplayImages() {
		return displayImages;
	}

	public void setDisplayImages(List<String> displayImages) {
		this.displayImages = displayImages;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public VariantInfoDto getVariantInfo() {
		return variantInfo;
	}

	public void setVariantInfo(VariantInfoDto variantInfo) {
		this.variantInfo = variantInfo;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(int sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public RatingDto getRating() {
		return rating;
	}

	public void setRating(RatingDto rating) {
		this.rating = rating;
	}

	public DetailDto getDetails() {
		return details;
	}

	public void setDetails(DetailDto details) {
		this.details = details;
	}

	public List<ReviewsDto> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewsDto> reviews) {
		this.reviews = reviews;
	}

	public List<LandingPageDto> getSimilarItems() {
		return similarItems;
	}

	public void setSimilarItems(List<LandingPageDto> similarItems) {
		this.similarItems = similarItems;
	}

	public PdpDto(List<String> displayImages, String brand, String product, VariantInfoDto variantInfo, int price,
			int sellingPrice, RatingDto rating, DetailDto details, List<ReviewsDto> reviews,
			List<LandingPageDto> similarItems) {
		super();
		this.displayImages = displayImages;
		this.brand = brand;
		this.product = product;
		this.variantInfo = variantInfo;
		this.price = price;
		this.sellingPrice = sellingPrice;
		this.rating = rating;
		this.details = details;
		this.reviews = reviews;
		this.similarItems = similarItems;
	}

	public PdpDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Nested DTO for variantInfo
	public static class VariantInfoDto {
		private String sku;
		private Set<String> color;
		private Set<String> size;

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public Set<String> getColor() {
			return color;
		}

		public void setColor(Set<String> color) {
			this.color = color;
		}

		public Set<String> getSize() {
			return size;
		}

		public void setSize(Set<String> size) {
			this.size = size;
		}

		public VariantInfoDto(String sku, Set<String> colors, Set<String> sizes) {
			super();
			this.sku = sku;
			this.color = colors;
			this.size = sizes;
		}

	}

	// Nested DTO for rating
	public static class RatingDto {
		private double avgRating;
		private double ratingCount;

		// Getters and Setters
		public double getAvgRating() {
			return avgRating;
		}

		public void setAvgRating(double avgRating) {
			this.avgRating = avgRating;
		}

		public double getRatingCount() {
			return ratingCount;
		}

		public void setRatingCount(double ratingCount) {
			this.ratingCount = ratingCount;
		}

		public RatingDto(double avgRating, double totalRatings) {
			super();
			this.avgRating = avgRating;
			this.ratingCount = totalRatings;
		}

	}

	// DTO for details
	public static class DetailDto {
//		private String title;
//		private String detail;
		private String description;
		private String fabric;
		private String fit;
		private String materialCare;
		private String seller;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getFabric() {
			return fabric;
		}

		public void setFabric(String fabric) {
			this.fabric = fabric;
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

		public String getSeller() {
			return seller;
		}

		public void setSeller(String seller) {
			this.seller = seller;
		}

		public DetailDto(String description, String fabric, String fit, String materialCare, String seller) {
			super();
			this.description = description;
			this.fabric = fabric;
			this.fit = fit;
			this.materialCare = materialCare;
			this.seller = seller;
		}

	}

	public static class ReviewsDto {
		private String username;
		private double rating;
		private String review;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public double getRating() {
			return rating;
		}

		public void setRating(double rating) {
			this.rating = rating;
		}

		public String getReview() {
			return review;
		}

		public void setReview(String review) {
			this.review = review;
		}

		public ReviewsDto(String username, double rating, String review) {
			super();
			this.username = username;
			this.rating = rating;
			this.review = review;
		}

		public ReviewsDto() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class SimilarItemDto {

		public SimilarItemDto() {
			super();
			// TODO Auto-generated constructor stub
		}

	}
}
