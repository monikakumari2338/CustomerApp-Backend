package com.deepanshu.modal;

public class ProductDetails {

	private String name;
	private int quantity;
	private String sku;
	private String Color;
	private String imageData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	// Method to decrease quantity
	public void decreaseQuantity(int amount) {
		if (this.quantity >= amount) {
			this.quantity -= amount;
		} else {
			throw new IllegalArgumentException("Insufficient stock for size " + name);
		}
	}

	public void increaseQuantity(int amount) {
		this.quantity += amount;
	}

	@Override
	public String toString() {
		return "ProductDetails [name=" + name + ", quantity=" + quantity + ", sku=" + sku + ", Color=" + Color
				+ ", imageData=" + imageData + "]";
	}

}
