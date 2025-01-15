package com.deepanshu.Dto;

public class CartItemsDto {

	private Long productId;
	private Long cartItemId;
	private String productName;
	private int quantity;
	private int pricePerUnit;
	private String imageUrl;
	private AttributesInfoDto attributes;
	// private AppliedPromosInfoDto appliedPromos;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(int pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public AttributesInfoDto getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributesInfoDto attributes) {
		this.attributes = attributes;
	}

	public CartItemsDto(Long productId, Long cartItemId, String productName, int quantity, int pricePerUnit,
			String imageUrl, AttributesInfoDto attributes) {
		super();
		this.productId = productId;
		this.cartItemId = cartItemId;
		this.productName = productName;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		this.imageUrl = imageUrl;
		this.attributes = attributes;
	}

	public CartItemsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Nested DTO for variantInfo
	public static class AttributesInfoDto {
		private String sku;
		private String color;
		private String size;

		public AttributesInfoDto() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public AttributesInfoDto(String sku, String color, String size) {
			super();
			this.sku = sku;
			this.color = color;
			this.size = size;
		}

	}

}
