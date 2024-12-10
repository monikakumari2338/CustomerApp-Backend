package com.deepanshu.Dto;

public class LandingPageDto {

	private Long id;
	private String imageData;
	private String brand;
	private String title;
	private int price;
	private int sellingPrice;
	private String sku;

	public LandingPageDto(Long id, String imageData, String brand, String title, int price, int sellingPrice,
			String sku) {
		super();
		this.id = id;
		this.imageData = imageData;
		this.brand = brand;
		this.title = title;
		this.price = price;
		this.sellingPrice = sellingPrice;
		this.sku = sku;
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
