package com.deepanshu.controller;

import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.User;
import com.deepanshu.service.RecommendationService;
import com.deepanshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public List<Product> getRecommendations(@PathVariable Long userId) {
        return recommendationService.recommendProductsForUser(userId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getRecommendationsForUser(@PathVariable Long userId) throws UserException {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }

        List<Product> recommendations = recommendationService.getRecommendations(user);

        return ResponseEntity.ok(recommendations);
    }
}
