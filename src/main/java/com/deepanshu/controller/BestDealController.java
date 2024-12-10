package com.deepanshu.controller;

import com.deepanshu.modal.BestDeal;
import com.deepanshu.service.BestDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bestdeals")
public class BestDealController {

    @Autowired
    private BestDealService bestDealService;

    @GetMapping("/best/{productId}")
    public ResponseEntity<BestDeal> getBestDeal(@PathVariable Long productId) {
        BestDeal bestDeal = bestDealService.calculateBestDeal(productId);
        return ResponseEntity.ok(bestDeal);
    }
}
