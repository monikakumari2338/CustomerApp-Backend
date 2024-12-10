package com.deepanshu.modal;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Set;

@Document(indexName = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductElasticSearch {

	@Id
	private String id;

	@Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
	private String name;

	@Field(type = FieldType.Text, name = "description")
	private String description;

	@Field(type = FieldType.Double, name = "price")
	private double price;

	@Field(type = FieldType.Double, name = "discountedPrice")
	private double discountedPrice;

	@Field(type = FieldType.Text, name = "brand")
	private String brand;

	@Field(type = FieldType.Text, name = "wearType")
	private String wearType;

	@Field(type = FieldType.Text, name = "fabric")
	private String fabric;

	@Field(type = FieldType.Text, name = "fit")
	private String fit;

	@Field(type = FieldType.Text, name = "materialCare")
	private String materialCare;

	@Field(type = FieldType.Text, name = "productCode")
	private String productCode;

	@Field(type = FieldType.Text, name = "seller")
	private String seller;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getWearType() {
		return wearType;
	}

	public void setWearType(String wearType) {
		this.wearType = wearType;
	}

	public String getFabric() {
		return fabric;
	}

	public void setFabric(String fabric) {
		this.fabric = fabric;
	}

	public String getFit() {
		return fit;
	}

	public void setFit(String fit) {
		this.fit = fit;
	}

	public String getMaterialCare() {
		return materialCare;
	}

	public void setMaterialCare(String materialCare) {
		this.materialCare = materialCare;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public ProductElasticSearch(String id, String name, String description, double price, double discountedPrice,
			String brand, String wearType, String fabric, String fit, String materialCare, String productCode,
			String seller) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.discountedPrice = discountedPrice;
		this.brand = brand;
		this.wearType = wearType;
		this.fabric = fabric;
		this.fit = fit;
		this.materialCare = materialCare;
		this.productCode = productCode;
		this.seller = seller;
	}

	// Removed topLevelCategory, secondLevelCategory, thirdLevelCategory, and images

}
