package com.deepanshu.controller;

import com.deepanshu.exception.ProductException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Order;
import com.deepanshu.modal.Product;
import com.deepanshu.modal.Subscription;
import com.deepanshu.modal.User;
import com.deepanshu.repository.SubscriptionRepository;
import com.deepanshu.request.CreateSubscriptionRequest;
import com.deepanshu.service.OrderService;
import com.deepanshu.service.ProductService;
import com.deepanshu.service.SubscriptionService;
import com.deepanshu.service.UserService;
import com.deepanshu.user.domain.DeliveryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/subscriptions")
    public ResponseEntity<Subscription> createSubscription(@RequestBody CreateSubscriptionRequest request) throws UserException {
        if (request.getUser() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        User user = userService.findUserById(request.getUser().getId());
        Subscription subscription = subscriptionService.createSubscription(
                user, request.getType(), request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(subscription);
    }

//    @PostMapping("/{subscriptionId}/schedule")
//    public ResponseEntity<?> scheduleDelivery(
//            @PathVariable Long subscriptionId,
//            @RequestParam Long productId,
//            @RequestParam List<LocalDate> deliveryDays,
//            @RequestParam LocalTime deliveryTime,
//            @RequestParam String deliveryComments,
//            @RequestParam(required = false,defaultValue = "false") boolean extendDelivery) throws ProductException {
//        Product product = productService.findProductById(productId);
//        Subscription subscription = subscriptionService.scheduleDelivery(subscriptionId,product, deliveryDays, deliveryTime, deliveryComments, extendDelivery);
//        return ResponseEntity.ok(subscription);
//    }

    @PostMapping("/{subscriptionId}/schedule")
    public ResponseEntity<?> scheduleDelivery(
            @PathVariable Long subscriptionId,
            @RequestParam Long productId,
            @RequestParam List<LocalDate> deliveryDays,
            @RequestParam LocalTime deliveryTime,
            @RequestParam String deliveryComments,
            @RequestParam(required = false, defaultValue = "false") boolean extendDelivery) {
        try {
            Product product = productService.findProductById(productId);
            Subscription subscription = subscriptionService.scheduleDelivery(subscriptionId, product, deliveryDays, deliveryTime, deliveryComments, extendDelivery);

            // Assuming you have a method to create an order for the subscription delivery
            Order order = orderService.createOrderForSubscriptionDelivery(subscription);

            return ResponseEntity.ok(order); // Return the created order
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to schedule delivery and create order: " + e.getMessage());
        }
    }


    @GetMapping("/subscriptions/{userId}")
public ResponseEntity<List<Subscription>> getAllSubscriptionForUser(@PathVariable Long userId) throws UserException {
    User user = userService.findUserById(userId);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    List<Subscription> userSubscriptions = subscriptionRepository.findActiveSubscriptionsByUser(user);
    return ResponseEntity.ok(userSubscriptions);
}


    @PostMapping("/{subscriptionId}/cancel")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok("Subscription canceled successfully.");
    }
    @PostMapping("/{subscriptionId}/extend")
    public ResponseEntity<Subscription> extendSubscription(@PathVariable Long subscriptionId,
                                                           @RequestParam("newEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newEndDate) {
        Subscription extendedSubscription = subscriptionService.extendSubscription(subscriptionId, newEndDate);
        return ResponseEntity.ok(extendedSubscription);
    }

    @PostMapping("/{subscriptionId}/pause")
    public ResponseEntity<String> pauseSubscription(@PathVariable Long subscriptionId) {
        Subscription subscription = subscriptionService.pauseSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body("Subscription with ID " + subscriptionId + " paused successfully.");
    }

    @PostMapping("/{subscriptionId}/resume")
    public ResponseEntity<String> resumeSubscription(@PathVariable Long subscriptionId) {
        Subscription subscription = subscriptionService.resumeSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body("Subscription with ID " + subscriptionId + " resumed successfully.");
    }

    @PutMapping("/{subscriptionId}/deliveries/{deliveryDate}/status")
    public ResponseEntity<String> updateDeliveryStatus(@PathVariable Long subscriptionId, @PathVariable LocalDate deliveryDate, @RequestParam DeliveryStatus status) {
        // Call the service method to update the delivery status
        try {
            subscriptionService.updateDeliveryStatus(subscriptionId, deliveryDate, status);
            return new ResponseEntity<>("Delivery status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}