package com.deepanshu.service;

import com.deepanshu.modal.Product;
import com.deepanshu.modal.User;

import java.util.List;

public interface RecommendationService {
    // Method to recommend products based on user's previous interactions (purchases, ratings)
    List<Product> recommendProductsForUser(Long userId);

    // Fetch recommendations based on order history, cart, and wishlist
    List<Product> getRecommendations(User user);
}
