package com.deepanshu.service;

import com.deepanshu.modal.BestDeal;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.Promotion;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.PromotionRepository;
import com.deepanshu.user.domain.DiscountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BestDealServiceImplementation implements BestDealService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public BestDeal calculateBestDeal(Long productId) {
return null;
    }

    private BigDecimal applyPromotion(BigDecimal price, Promotion promotion) {
     return  null;
    }

}
