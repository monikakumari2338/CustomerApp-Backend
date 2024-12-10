package com.deepanshu.modal;


import com.deepanshu.user.domain.OrderStatus;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private String orderId;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    @OneToOne
    private Address shippingAddress;
    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

    private double totalPrice;

    private Integer totalDiscountedPrice;

    private Integer discounte;

    private OrderStatus orderStatus;

    private int totalItem;

    private LocalDateTime createdAt;

    @Column(name = "redeemed_points")
    private int redeemedPoints;

    private int usedPoints;
    @ManyToOne
    private Subscription subscription;
    @ManyToOne
    private StorePickup storePickup;

    @ManyToOne
    @JoinColumn(name = "cancellation_reason_id")
    private CancellationReason cancellationReason;

    @ManyToOne
    @JoinColumn(name = "return_reason_id")
    private ReturnReason returnReason;

    private String comments;

    private String attachment;

    @ManyToOne
    private Order originalOrder;

    @Column(name = "promotion_discount")
    private BigDecimal promotionDiscount;

    //get applied promotion on order
    @Transient
    private Map<String,Integer> promotionList=new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "order_promo_codes", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "promo_code")
    @Column(name = "discount_value")
    private Map<String, Double> promoCode = new HashMap<>();

    @Column(name="filteredSum")
    private String filteredSum;


    public Order() {
    }

    public Order(String orderId, User user, List<OrderItem> orderItems, LocalDateTime orderDate, LocalDateTime deliveryDate, Address shippingAddress, PaymentDetails paymentDetails, double totalPrice, Integer totalDiscountedPrice, Integer discounte, OrderStatus orderStatus, int totalItem, LocalDateTime createdAt, int redeemedPoints, int usedPoints, Subscription subscription, StorePickup storePickup, CancellationReason cancellationReason, ReturnReason returnReason, String comments, String attachment, Order originalOrder, BigDecimal promotionDiscount, Map<String, Integer> promotionList, Map<String, Double> promoCode, String filteredSum) {
        this.orderId = orderId;
        this.user = user;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.shippingAddress = shippingAddress;
        this.paymentDetails = paymentDetails;
        this.totalPrice = totalPrice;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.discounte = discounte;
        this.orderStatus = orderStatus;
        this.totalItem = totalItem;
        this.createdAt = createdAt;
        this.redeemedPoints = redeemedPoints;
        this.usedPoints = usedPoints;
        this.subscription = subscription;
        this.storePickup = storePickup;
        this.cancellationReason = cancellationReason;
        this.returnReason = returnReason;
        this.comments = comments;
        this.attachment = attachment;
        this.originalOrder = originalOrder;
        this.promotionDiscount = promotionDiscount;
        this.promotionList = promotionList;
        this.promoCode = promoCode;
        this.filteredSum = filteredSum;
    }

    public String getFilteredSum() {
        return filteredSum;
    }

    public void setFilteredSum(String filteredSum) {
        this.filteredSum = filteredSum;
    }

    public Map<String, Double> getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(Map<String, Double> promoCode) {
        this.promoCode = promoCode;
    }

    public Map<String, Integer> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(Map<String, Integer> promotionList) {
        this.promotionList = promotionList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public Integer getDiscounte() {
        return discounte;
    }

    public void setDiscounte(Integer discounte) {
        this.discounte = discounte;
    }

    public Integer getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public void setTotalDiscountedPrice(Integer totalDiscountedPrice) {
        this.totalDiscountedPrice = totalDiscountedPrice;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getRedeemedPoints() {
        return redeemedPoints;
    }

    public void setRedeemedPoints(int redeemedPoints) {
        this.redeemedPoints = redeemedPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public void setUsedPoints(int usedPoints) {
        this.usedPoints = usedPoints;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public StorePickup getStorePickup() {
        return storePickup;
    }

    public void setStorePickup(StorePickup storePickup) {
        this.storePickup = storePickup;
    }

    public CancellationReason getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(CancellationReason cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public ReturnReason getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(ReturnReason returnReason) {
        this.returnReason = returnReason;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Order getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(Order originalOrder) {
        this.originalOrder = originalOrder;
    }

    public BigDecimal getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(BigDecimal promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }
}
