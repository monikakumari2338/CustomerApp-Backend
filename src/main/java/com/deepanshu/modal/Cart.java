package com.deepanshu.modal;

import java.math.BigDecimal;
import java.util.*;
import jakarta.persistence.*;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "cart_items")
    private Set<CartItem> cartItems = new HashSet<>();

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_item")
    private int totalItem;

    private int totalDiscountedPrice;

    private int discounte;

    private BigDecimal promotion_discount;

    @Column(name = "express_delivery")
    private boolean expressDelivery;



    @ElementCollection
    @Column(name="promotion_code")
    @MapKeyColumn(name = "promo_code")
    private Map<String, Double> promoCode = new HashMap<>();


    @Column(name = "bagTotalPrice")
    private double bagTotalPrice=0.0;
    @Column(name = "totalSaving")
    private double totalSaving=0.0;
    @Column(name = "deliveryCharge")
    private String deliveryCharge;
    @Column(name = "totalDiscount")
    private double totalCartDiscount=0.0;
    @Column(name = "totalRedeemPoints")
    private double redeemPoints=0.0;


    public Cart() {
    }


    public Cart(User user, Set<CartItem> cartItems, double totalPrice, int totalItem, int totalDiscountedPrice, int discounte, BigDecimal promotion_discount, boolean expressDelivery, Map<String, Double> promoCode, double bagTotalPrice, double totalSaving, String deliveryCharge, double totalCartDiscount, double redeemPoints) {
        this.user = user;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.totalItem = totalItem;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.discounte = discounte;
        this.promotion_discount = promotion_discount;
        this.expressDelivery = expressDelivery;
        this.promoCode = promoCode;
        this.bagTotalPrice = bagTotalPrice;
        this.totalSaving = totalSaving;
        this.deliveryCharge = deliveryCharge;
        this.totalCartDiscount = totalCartDiscount;
        this.redeemPoints = redeemPoints;
    }

    public Map<String, Double> getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(Map<String, Double> promoCode) {
        this.promoCode = promoCode;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public double getRedeemPoints() {
        return redeemPoints;
    }

    public void setRedeemPoints(double redeemPoints) {
        this.redeemPoints = redeemPoints;
    }

    public double getTotalCartDiscount() {
        return totalCartDiscount;
    }

    public void setTotalCartDiscount(double totalCartDiscount) {
        this.totalCartDiscount = totalCartDiscount;
    }

    public double getBagTotalPrice() {
        return bagTotalPrice;
    }

    public void setBagTotalPrice(double bagTotalPrice) {
        this.bagTotalPrice = bagTotalPrice;
    }

    public double getTotalSaving() {
        return totalSaving;
    }

    public void setTotalSaving(double totalSaving) {
        this.totalSaving = totalSaving;
    }

    public int getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public void setTotalDiscountedPrice(int totalDiscountedPrice) {
        this.totalDiscountedPrice = totalDiscountedPrice;
    }

    public int getDiscounte() {
        return discounte;
    }

    public void setDiscounte(int discounte) {
        this.discounte = discounte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public boolean isExpressDelivery() {
        return expressDelivery;
    }

    public void setExpressDelivery(boolean expressDelivery) {
        this.expressDelivery = expressDelivery;
    }

    public BigDecimal getPromotion_discount() {
        return promotion_discount;
    }

    public void setPromotion_discount(BigDecimal promotion_discount) {
        this.promotion_discount = promotion_discount;
    }
    
}
