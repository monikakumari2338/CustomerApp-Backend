package com.deepanshu.request;

import com.deepanshu.modal.CategoryPair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PromotionRequest {
	private List<Long> eligibleProductId;
	private List<Long> nonEligibleProductId;
	private String productCategory;
	private String promotionCode;
	private List<CategoryPair> appliedCategory;

	public List<Long> getEligibleProductId() {
		return eligibleProductId;
	}

	public void setEligibleProductId(List<Long> eligibleProductId) {
		this.eligibleProductId = eligibleProductId;
	}

	public List<Long> getNonEligibleProductId() {
		return nonEligibleProductId;
	}

	public void setNonEligibleProductId(List<Long> nonEligibleProductId) {
		this.nonEligibleProductId = nonEligibleProductId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public List<CategoryPair> getAppliedCategory() {
		return appliedCategory;
	}

	public void setAppliedCategory(List<CategoryPair> appliedCategory) {
		this.appliedCategory = appliedCategory;
	}

	public PromotionRequest(List<Long> eligibleProductId, List<Long> nonEligibleProductId, String productCategory,
			String promotionCode, List<CategoryPair> appliedCategory) {
		super();
		this.eligibleProductId = eligibleProductId;
		this.nonEligibleProductId = nonEligibleProductId;
		this.productCategory = productCategory;
		this.promotionCode = promotionCode;
		this.appliedCategory = appliedCategory;
	}

	public PromotionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
