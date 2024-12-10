package com.deepanshu.request;

import java.util.*;

import com.deepanshu.modal.ProductDetails;

public class CreateProductRequest {

	private String title;

	private String description;

	private int price;

	private int discountedPrice;

	private int discountPercent;

	private int quantity;

	private String brand;

	private String color;

	private String country;
	private String wearType;
	private String fabric;
	private String sleeves;
	private String fit;
	private String materialCare;
	private String productCode;
	private String seller;
	private String sellerInfo;

	private String ingredient;
	private String packaging;
	private String milktype;
	private String genericname;
	private String countryoforigin;
	private String preservatives;
	private String consumewithin;
	private Set<ProductDetails> productDetails = new HashSet<>();
	private Set<String> imageUrl;
	private String topLavelCategory;
	private String secondLavelCategory;
	private String thirdLavelCategory;
	private List<String> pincode;

	private boolean eligibleForBogo;

	public boolean isEligibleForBogo() {
		return eligibleForBogo;
	}

	public void setEligibleForBogo(boolean eligibleForBogo) {
		this.eligibleForBogo = eligibleForBogo;
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

	public Set<ProductDetails> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Set<ProductDetails> productDetails) {
		this.productDetails = productDetails;
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

	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(int discountedPrice) {
		this.discountedPrice = discountedPrice;
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

	public Set<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(Set<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTopLavelCategory() {
		return topLavelCategory;
	}

	public void setTopLavelCategory(String topLavelCategory) {
		this.topLavelCategory = topLavelCategory;
	}

	public String getSecondLavelCategory() {
		return secondLavelCategory;
	}

	public void setSecondLavelCategory(String secondLavelCategory) {
		this.secondLavelCategory = secondLavelCategory;
	}

	public String getThirdLavelCategory() {
		return thirdLavelCategory;
	}

	public void setThirdLavelCategory(String thirdLavelCategory) {
		this.thirdLavelCategory = thirdLavelCategory;
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
}
