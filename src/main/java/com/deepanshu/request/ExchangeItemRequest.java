package com.deepanshu.request;

public class ExchangeItemRequest {
    private Long orderItemId;
    private Long newProductId;
    private int quantity;
    private String size;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getNewProductId() {
        return newProductId;
    }

    public void setNewProductId(Long newProductId) {
        this.newProductId = newProductId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
