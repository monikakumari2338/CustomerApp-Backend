package com.deepanshu.service;

import com.deepanshu.modal.Promotion;
import com.deepanshu.modal.PromotionDTO;
import com.deepanshu.request.PromotionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PromotionService {
  //create 5% OFF Promotion transactionWise
  Promotion createPromotion(PromotionDTO promotionDTO);

  //get all promotion
  List<Promotion> getAllPromotions();

  //remove promotion byId
   Promotion removePromotionById(Long promotionId);


  //apply fivePercentOff on productId's
  String applyPromotionOnProductId(PromotionRequest promotionRequest);

  //remove fivePercentOff on productId's
  String removePromotionFromProductId(PromotionRequest promotionRequest);
}