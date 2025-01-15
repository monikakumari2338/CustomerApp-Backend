package com.deepanshu.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddItemRequest {
	private String sku;
	private int quantity;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public AddItemRequest(String sku, int quantity) {
		super();
		this.sku = sku;
		this.quantity = quantity;
	}

	public AddItemRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
