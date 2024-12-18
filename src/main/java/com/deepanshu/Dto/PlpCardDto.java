package com.deepanshu.Dto;

import java.util.List;
import java.util.Set;

public class PlpCardDto {

	private Long id;
	private List<String> image;
	private String brandName;
	private String itemName;
	private int price;
	private int sellingPrice;
	private double rating;
	private Set<String> colors;
	private Set<String> sizes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
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

	public Set<String> getColors() {
		return colors;
	}

	public void setColors(Set<String> colors) {
		this.colors = colors;
	}

	public Set<String> getSizes() {
		return sizes;
	}

	public void setSizes(Set<String> sizes) {
		this.sizes = sizes;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public PlpCardDto(Long id, List<String> image, String brandName, String itemName, int price, int sellingPrice,
			double rating, Set<String> colors, Set<String> sizes) {
		super();
		this.id = id;
		this.image = image;
		this.brandName = brandName;
		this.itemName = itemName;
		this.price = price;
		this.sellingPrice = sellingPrice;
		this.rating = rating;
		this.colors = colors;
		this.sizes = sizes;
	}

}
