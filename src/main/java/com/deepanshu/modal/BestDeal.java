package com.deepanshu.modal;

import java.math.BigDecimal;

public class BestDeal {
    private BigDecimal bestPrice;
    private BigDecimal bestDiscount;
    private String promotionDetails;

    public BestDeal(BigDecimal bestPrice, BigDecimal bestDiscount, String promotionDetails) {
        this.bestPrice = bestPrice;
        this.bestDiscount = bestDiscount;
        this.promotionDetails = promotionDetails;
    }

    // Getters and setters
    public BigDecimal getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(BigDecimal bestPrice) {
        this.bestPrice = bestPrice;
    }

    public BigDecimal getBestDiscount() {
        return bestDiscount;
    }

    public void setBestDiscount(BigDecimal bestDiscount) {
        this.bestDiscount = bestDiscount;
    }

    public String getPromotionDetails() {
        return promotionDetails;
    }

    public void setPromotionDetails(String promotionDetails) {
        this.promotionDetails = promotionDetails;
    }
}
