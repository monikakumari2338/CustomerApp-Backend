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
	private Long productId;
	private String size;
	private int quantity;
	private Integer price;
	private String category;
	private String color;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public AddItemRequest(Long productId, String size, int quantity, Integer price, String category, String color) {
		super();
		this.productId = productId;
		this.size = size;
		this.quantity = quantity;
		this.price = price;
		this.category = category;
		this.color = color;
	}

	public AddItemRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
