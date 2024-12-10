package com.deepanshu.Dto;

import java.util.List;

public class PlpCardDto {

	private Long id;
	private String image;
	private String brandName;
	private String itemName;
	private int price;
	private int sellingPrice;
	private String sku;
	private List<String> colors;
	private List<String> sizes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public List<String> getSizes() {
		return sizes;
	}

	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}

	public PlpCardDto(Long id, String image, String brandName, String itemName, int price, int sellingPrice, String sku,
			List<String> colors, List<String> sizes) {
		super();
		this.id = id;
		this.image = image;
		this.brandName = brandName;
		this.itemName = itemName;
		this.price = price;
		this.sellingPrice = sellingPrice;
		this.sku = sku;
		this.colors = colors;
		this.sizes = sizes;
	}

}
