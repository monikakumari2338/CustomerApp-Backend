package com.deepanshu.request;

import java.time.LocalDateTime;
import java.util.List;

public class PickupRequest {
    private LocalDateTime pickupDateTime;
    private String comment;
    private Long userId;
    private List<Long> productIds;
    private List<String> sizeNames;
    private List<Integer> quantities;

    // Getters and setters

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<String> getSizeNames() {
        return sizeNames;
    }

    public void setSizeNames(List<String> sizeNames) {
        this.sizeNames = sizeNames;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }
}
