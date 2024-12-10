package com.deepanshu.modal;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class ContinueShoppingItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cSId;

	@ManyToOne()
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	@ManyToOne()
	@JoinColumn(name = "productId", referencedColumnName = "id")
	private Product product;
	private LocalDateTime createdAt;
	private String sku;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public ContinueShoppingItems(User user, Product product, LocalDateTime createdAt, String sku) {
		super();

		this.user = user;
		this.product = product;
		this.createdAt = createdAt;
		this.sku = sku;
	}

	public ContinueShoppingItems() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ContinueShoppingItems [cSId=" + cSId + ", user=" + user + ", product=" + product + ", createdAt="
				+ createdAt + ", sku=" + sku + "]";
	}

}
