package com.deepanshu.service;

import com.deepanshu.modal.*;
import com.deepanshu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImplementation implements RecommendationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private WishlistRepository wishlistRepository;

    // Method to recommend products based on user's previous interactions (purchases, ratings)
    @Override
    public List<Product> recommendProductsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ArrayList<>();  // Return empty list if user not found
        }

        // Fetch products the user has rated or purchased
        Set<Product> productsUserInteractedWith = getProductsFromUserInteractions(user);

        // Fetch similar products by categories, brands, or other properties
        Set<Product> recommendedProducts = getSimilarProducts(productsUserInteractedWith);

        // Exclude products the user has already interacted with
        return recommendedProducts.stream()
                .filter(product -> !productsUserInteractedWith.contains(product))
                .collect(Collectors.toList());
    }

    // Fetch products from user's ratings, reviews, or orders (depending on your model)
    private Set<Product> getProductsFromUserInteractions(User user) {
        Set<Product> interactedProducts = new HashSet<>();

        // Example: Add products from user's ratings (you can add other criteria like purchases)
     //   user.getRatings().forEach(rating -> interactedProducts.add(rating.getProduct()));

        // Example: Add products from user's reviews
        user.getReviews().forEach(review -> interactedProducts.add(review.getProduct()));

        return interactedProducts;
    }

    // Fetch similar products based on brand, category, etc.
    private Set<Product> getSimilarProducts(Set<Product> products) {
        Set<Product> similarProducts = new HashSet<>();

        // Find products by similar categories or brands
        products.forEach(product -> {
            similarProducts.addAll(productRepository.findByCategoryOrBrand(
                    product.getCategory(), product.getBrand()));
        });

        return similarProducts;
    }

    // Fetch recommendations based on order history, cart, and wishlist
    @Override
    public List<Product> getRecommendations(User user) {
        List<Product> recommendations = new ArrayList<>();

        // 1. Based on order history
        List<Order> userOrders = orderRepository.findByUser(user);
        for (Order order : userOrders) {
            for (OrderItem item : order.getOrderItems()) {
                // Logic to recommend similar products based on past purchases
                List<Product> similarProducts = findSimilarProducts(item.getProduct());
                System.out.println("Similar products based on order item: " + item.getProduct().getTitle() + ": " + similarProducts);
                recommendations.addAll(similarProducts);
            }
        }

        // 2. Based on cart activity
        Cart userCart = cartRepository.findByUser(user);
        for (CartItem cartItem : userCart.getCartItems()) {
            // Logic to recommend add-on or complementary products
            List<Product> addOnProducts = findSimilarProducts(cartItem.getProduct());
            System.out.println("Similar products for cart item: " + cartItem.getProduct().getTitle() + ": " + addOnProducts);
            recommendations.addAll(addOnProducts);
        }

        // 3. Based on wishlist
        Wishlist userWishlist = wishlistRepository.findByUser(user);
        for (WishlistItem wishlistItem : userWishlist.getWishlistItems()) {
            // Logic to recommend similar products from the wishlist
            List<Product> similarWishlistProducts = findSimilarProducts(wishlistItem.getProduct());
            System.out.println("Similar products based on wishlist item: " + wishlistItem.getProduct().getTitle() + ": " + similarWishlistProducts);
            recommendations.addAll(similarWishlistProducts);
        }

        return recommendations;
    }

    private List<Product> findSimilarProducts(Product product) {
        // Fetch products with similar categories or brands
        List<Product> similarProductsByCategoryAndBrand = (List<Product>) productRepository
                .findByCategoryOrBrand(product.getCategory(), product.getBrand());

        System.out.println("Similar products for " + product.getTitle() + ": " + similarProductsByCategoryAndBrand);

        // Further refine based on price range (optional)
        List<Product> filteredByPriceRange = similarProductsByCategoryAndBrand.stream()
                .filter(p -> Math.abs(p.getPrice() - product.getPrice()) <= 500)
                .collect(Collectors.toList());

        System.out.println("Filtered products by price: " + filteredByPriceRange);

        return filteredByPriceRange;
    }
}
