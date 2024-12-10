package com.deepanshu.modal;

import ch.qos.logback.core.joran.sanity.Pair;
import com.deepanshu.user.domain.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "promotions")

public class Promotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "promotion_name")
	private String promotionName;

	@Column(name = "promotionDescription")
	private String promotionDescription;

	@Column(name = "promotionCode")
	private String promotionCode;

	@Column(name = "promotionType")
	private String promotionType;

	@Column(name = "discountValue")
	private String discountValue;

	@Column(name = "startDate")
	private LocalDateTime promotionStartdate;

	@Column(name = "endDate")
	private LocalDateTime promotionEndDate;

	@Column(name = "promotionMinOrderValue")
	private double minOrderValue;

	@Column(name = "promotionMaxOrderValue")
	private double maxOrderValue;

	@Column(name = "maxDiscount")
	private double maxDiscountOnCart;

	@Column(name = "promotionUsageLimit")
	private int usageLimit;

	@Column(name = "promotionUsageCount")
	private int usageCount;

	@Column(name = "isPromotionActive")
	private boolean promotionActive;

	@Column(name = "temporarelyInactive")
	private boolean temporarilyInactive;

	@ElementCollection
	@CollectionTable(name = "promotion_categories", joinColumns = @JoinColumn(name = "promotion_id"))
	private List<CategoryPair> appliedCategory = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "includedProductId", joinColumns = @JoinColumn(name = "promotion_id"))
	@Column(name = "includedProductId")
	private List<Long> includedProductList = new ArrayList<>();

	@Column(name = "exludedProductId")
	private List<Long> excludedFromPromotionList = new ArrayList<>();

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDateTime getPromotionStartdate() {
		return promotionStartdate;
	}

	public void setPromotionStartdate(LocalDateTime promotionStartdate) {
		this.promotionStartdate = promotionStartdate;
	}

	public LocalDateTime getPromotionEndDate() {
		return promotionEndDate;
	}

	public void setPromotionEndDate(LocalDateTime promotionEndDate) {
		this.promotionEndDate = promotionEndDate;
	}

	public double getMinOrderValue() {
		return minOrderValue;
	}

	public void setMinOrderValue(double minOrderValue) {
		this.minOrderValue = minOrderValue;
	}

	public double getMaxOrderValue() {
		return maxOrderValue;
	}

	public void setMaxOrderValue(double maxOrderValue) {
		this.maxOrderValue = maxOrderValue;
	}

	public double getMaxDiscountOnCart() {
		return maxDiscountOnCart;
	}

	public void setMaxDiscountOnCart(double maxDiscountOnCart) {
		this.maxDiscountOnCart = maxDiscountOnCart;
	}

	public int getUsageLimit() {
		return usageLimit;
	}

	public void setUsageLimit(int usageLimit) {
		this.usageLimit = usageLimit;
	}

	public int getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
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

	public List<Long> getIncludedProductList() {
		return includedProductList;
	}

	public void setIncludedProductList(List<Long> includedProductList) {
		this.includedProductList = includedProductList;
	}

	public List<Long> getExcludedFromPromotionList() {
		return excludedFromPromotionList;
	}

	public void setExcludedFromPromotionList(List<Long> excludedFromPromotionList) {
		this.excludedFromPromotionList = excludedFromPromotionList;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Promotion(Long id, String promotionName, String promotionDescription, String promotionCode,
			String promotionType, String discountValue, LocalDateTime promotionStartdate,
			LocalDateTime promotionEndDate, double minOrderValue, double maxOrderValue, double maxDiscountOnCart,
			int usageLimit, int usageCount, boolean promotionActive, boolean temporarilyInactive,
			List<CategoryPair> appliedCategory, List<Long> includedProductList, List<Long> excludedFromPromotionList,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.promotionName = promotionName;
		this.promotionDescription = promotionDescription;
		this.promotionCode = promotionCode;
		this.promotionType = promotionType;
		this.discountValue = discountValue;
		this.promotionStartdate = promotionStartdate;
		this.promotionEndDate = promotionEndDate;
		this.minOrderValue = minOrderValue;
		this.maxOrderValue = maxOrderValue;
		this.maxDiscountOnCart = maxDiscountOnCart;
		this.usageLimit = usageLimit;
		this.usageCount = usageCount;
		this.promotionActive = promotionActive;
		this.temporarilyInactive = temporarilyInactive;
		this.appliedCategory = appliedCategory;
		this.includedProductList = includedProductList;
		this.excludedFromPromotionList = excludedFromPromotionList;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Promotion() {
		super();
		// TODO Auto-generated constructor stub
	}

}
