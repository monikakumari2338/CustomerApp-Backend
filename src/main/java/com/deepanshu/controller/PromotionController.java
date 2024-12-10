package com.deepanshu.controller;

import com.deepanshu.modal.Promotion;
import com.deepanshu.modal.PromotionDTO;
import com.deepanshu.request.PromotionRequest;
import com.deepanshu.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("/createPromotion")
    public ResponseEntity<Promotion> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        Promotion createdPromotion = promotionService.createPromotion(promotionDTO);
        return new ResponseEntity<>(createdPromotion, HttpStatus.CREATED);
    }

    // API to get all promotions
    @GetMapping("/all")
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
    	System.out.println(promotionService.getAllPromotions());
        return ResponseEntity.ok(promotions); // Return the list of promotions with a 200 OK status
    }


    // API to remove a promotion by its ID
    @DeleteMapping("/remove/{promotionId}")
    public ResponseEntity<String> removePromotionById(@PathVariable Long promotionId) {
        Promotion removedPromotion = promotionService.removePromotionById(promotionId);
        return ResponseEntity.ok("Promotion with ID " + promotionId + " has been removed successfully.");
    }

    //apply fivePercentOff on productId's
    @PostMapping("/applyPromotion")
    public String applyPromotion(@RequestBody PromotionRequest promotionRequest){
        return promotionService.applyPromotionOnProductId(promotionRequest);
    }
    //remove fivePercentOff on productId's
    @PutMapping("/removePromotion")
    public ResponseEntity<String> removePromotion(@RequestBody PromotionRequest promotionRequest) {
        String result = promotionService.removePromotionFromProductId(promotionRequest);
        return ResponseEntity.ok(result);
    }
}
