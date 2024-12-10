package com.deepanshu.modal;

import java.util.List;

public class PromotionDTO {

	private String promotionName;
	private String promotionDescription;
	private String promotionCode;
	private String promotionType; // Percentage, Flat etc.
	private String discountValue;
	private String promotionEndDate; // String for the input format "dd/MM/yyyy"
	private double promotionMinOrderValue;
	private double promotionMaxOrderValue;
	private double maxDiscountOnCart;
	private int promotionUsageLimit;
	private int promotionUsageCount;
	private boolean promotionActive;
	private boolean temporarilyInactive;
	private List<CategoryPair> appliedCategory; // List of category pairs
	private List<Long> excludedFromPromotionList; // List of excluded product IDs

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}

	public String getPromotionEndDate() {
		return promotionEndDate;
	}

	public void setPromotionEndDate(String promotionEndDate) {
		this.promotionEndDate = promotionEndDate;
	}

	public double getPromotionMinOrderValue() {
		return promotionMinOrderValue;
	}

	public void setPromotionMinOrderValue(double promotionMinOrderValue) {
		this.promotionMinOrderValue = promotionMinOrderValue;
	}

	public double getPromotionMaxOrderValue() {
		return promotionMaxOrderValue;
	}

	public void setPromotionMaxOrderValue(double promotionMaxOrderValue) {
		this.promotionMaxOrderValue = promotionMaxOrderValue;
	}

	public double getMaxDiscountOnCart() {
		return maxDiscountOnCart;
	}

	public void setMaxDiscountOnCart(double maxDiscountOnCart) {
		this.maxDiscountOnCart = maxDiscountOnCart;
	}

	public int getPromotionUsageLimit() {
		return promotionUsageLimit;
	}

	public void setPromotionUsageLimit(int promotionUsageLimit) {
		this.promotionUsageLimit = promotionUsageLimit;
	}

	public int getPromotionUsageCount() {
		return promotionUsageCount;
	}

	public void setPromotionUsageCount(int promotionUsageCount) {
		this.promotionUsageCount = promotionUsageCount;
	}

	public boolean isPromotionActive() {
		return promotionActive;
	}

	public void setPromotionActive(boolean promotionActive) {
		this.promotionActive = promotionActive;
	}

	public boolean isTemporarilyInactive() {
		return temporarilyInactive;
	}

	public void setTemporarilyInactive(boolean temporarilyInactive) {
		this.temporarilyInactive = temporarilyInactive;
	}

	public List<CategoryPair> getAppliedCategory() {
		return appliedCategory;
	}

	public void setAppliedCategory(List<CategoryPair> appliedCategory) {
		this.appliedCategory = appliedCategory;
	}

	public List<Long> getExcludedFromPromotionList() {
		return excludedFromPromotionList;
	}

	public void setExcludedFromPromotionList(List<Long> excludedFromPromotionList) {
		this.excludedFromPromotionList = excludedFromPromotionList;
	}

	public PromotionDTO(String promotionName, String promotionDescription, String promotionCode, String promotionType,
			String discountValue, String promotionEndDate, double promotionMinOrderValue, double promotionMaxOrderValue,
			double maxDiscountOnCart, int promotionUsageLimit, int promotionUsageCount, boolean promotionActive,
			boolean temporarilyInactive, List<CategoryPair> appliedCategory, List<Long> excludedFromPromotionList) {
		super();
		this.promotionName = promotionName;
		this.promotionDescription = promotionDescription;
		this.promotionCode = promotionCode;
		this.promotionType = promotionType;
		this.discountValue = discountValue;
		this.promotionEndDate = promotionEndDate;
		this.promotionMinOrderValue = promotionMinOrderValue;
		this.promotionMaxOrderValue = promotionMaxOrderValue;
		this.maxDiscountOnCart = maxDiscountOnCart;
		this.promotionUsageLimit = promotionUsageLimit;
		this.promotionUsageCount = promotionUsageCount;
		this.promotionActive = promotionActive;
		this.temporarilyInactive = temporarilyInactive;
		this.appliedCategory = appliedCategory;
		this.excludedFromPromotionList = excludedFromPromotionList;
	}

	public PromotionDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
