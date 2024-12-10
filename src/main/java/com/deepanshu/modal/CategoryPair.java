package com.deepanshu.modal;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CategoryPair {
	private String brandName;
	private String brandCategory;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandCategory() {
		return brandCategory;
	}

	public void setBrandCategory(String brandCategory) {
		this.brandCategory = brandCategory;
	}

	public CategoryPair(String brandName, String brandCategory) {
		super();
		this.brandName = brandName;
		this.brandCategory = brandCategory;
	}

	public CategoryPair() {
		super();
		// TODO Auto-generated constructor stub
	}

}