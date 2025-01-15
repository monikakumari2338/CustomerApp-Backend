package com.deepanshu.Dto;

import java.util.Set;

public class CartDto {

	private Long cartId;
	private Long userId;
	private Set<CartItemsDto> items;
	private AppliedPromosInfoDto appliedPromos;
	private int totalItems;
	private int totalQuantity;
	private double totalAmount;
	private int totalDiscountedPrice;
	private String deliveryCharge;

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<CartItemsDto> getItems() {
		return items;
	}

	public void setItems(Set<CartItemsDto> items) {
		this.items = items;
	}

	public AppliedPromosInfoDto getAppliedPromos() {
		return appliedPromos;
	}

	public void setAppliedPromos(AppliedPromosInfoDto appliedPromos) {
		this.appliedPromos = appliedPromos;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getTotalDiscountedPrice() {
		return totalDiscountedPrice;
	}

	public void setTotalDiscountedPrice(int totalDiscountedPrice) {
		this.totalDiscountedPrice = totalDiscountedPrice;
	}

	public String getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(String deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public CartDto(Long cartId, Long userId, Set<CartItemsDto> items, AppliedPromosInfoDto appliedPromos,
			int totalItems, int totalQuantity, double totalAmount, int totalDiscountedPrice, String deliveryCharge) {
		super();
		this.cartId = cartId;
		this.userId = userId;
		this.items = items;
		this.appliedPromos = appliedPromos;
		this.totalItems = totalItems;
		this.totalQuantity = totalQuantity;
		this.totalAmount = totalAmount;
		this.totalDiscountedPrice = totalDiscountedPrice;
		this.deliveryCharge = deliveryCharge;
	}

	@Override
	public String toString() {
		return "CartDto [cartId=" + cartId + ", userId=" + userId + ", items=" + items + ", appliedPromos="
				+ appliedPromos + ", totalItems=" + totalItems + ", totalQuantity=" + totalQuantity + ", totalAmount="
				+ totalAmount + ", totalDiscountedPrice=" + totalDiscountedPrice + ", deliveryCharge=" + deliveryCharge
				+ "]";
	}

	public static class AppliedPromosInfoDto {
		private String promoCode;
		private double discountAmount;
		private String promoType;

		public String getPromoCode() {
			return promoCode;
		}

		public void setPromoCode(String promoCode) {
			this.promoCode = promoCode;
		}

		public double getDiscountAmount() {
			return discountAmount;
		}

		public void setDiscountAmount(double discountAmount) {
			this.discountAmount = discountAmount;
		}

		public String getPromoType() {
			return promoType;
		}

		public void setPromoType(String promoType) {
			this.promoType = promoType;
		}

		public AppliedPromosInfoDto(String promoCode, double discountAmount, String promoType) {
			super();
			this.promoCode = promoCode;
			this.discountAmount = discountAmount;
			this.promoType = promoType;
		}

	}

}
