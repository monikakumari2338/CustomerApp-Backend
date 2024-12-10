package com.deepanshu.Dto;

public class LandingPageDto {

	private Long id;
	private String image;
	private String brand;
	private String product;
	private int price;
	private int sellingPrice;

	public LandingPageDto(Long id, String image, String brand, String product, int price, int sellingPrice) {
		super();
		this.id = id;
		this.image = image;
		this.brand = brand;
		this.product = product;
		this.price = price;
		this.sellingPrice = sellingPrice;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
