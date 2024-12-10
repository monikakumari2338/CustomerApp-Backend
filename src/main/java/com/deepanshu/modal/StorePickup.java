package com.deepanshu.modal;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class StorePickup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private LocalDateTime pickupDateTime;

    private String comment;
    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private ProductDetails size;


    public StorePickup() {

    }

    public StorePickup(Long id, Store store, LocalDateTime pickupDateTime) {
        this.id = id;
        this.store = store;
        this.pickupDateTime = pickupDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public LocalDateTime getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(LocalDateTime pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public ProductDetails getSize() {
        return size;
    }

    public void setSize(ProductDetails size) {
        this.size = size;
    }
}
