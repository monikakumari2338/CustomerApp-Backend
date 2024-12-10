package com.deepanshu.service;

import com.deepanshu.exception.NotFoundException;
import com.deepanshu.modal.*;
import com.deepanshu.repository.*;
import com.deepanshu.user.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImplementation implements SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private FreezeAmountRepository freezeAmountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Subscription createSubscription(User user, SubscriptionType type, LocalDate startDate, LocalDate endDate) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setType(type);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        return subscriptionRepository.save(subscription);
    }

//    @Override
//    public Subscription scheduleDelivery(Long subscriptionId, Product product, List<LocalDate> deliveryDays, LocalTime deliveryTime, String deliveryComments, boolean extendDelivery) {
//        // Retrieve the subscription from the database
//        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
//        if (optionalSubscription.isEmpty()) {
//            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
//        }
//        Subscription subscription = optionalSubscription.get();
//
//        // Extend delivery days only if the user wants
//        if (extendDelivery) {
//            subscription.setDeliveryDays(new ArrayList<>());
//            subscription.getDeliveryDays().addAll(deliveryDays);
//        } else {
//            subscription.setDeliveryDays(deliveryDays);
//        }
//
//        subscription.setProduct(product);
//        subscription.setDeliveryTime(deliveryTime);
//        subscription.setDeliveryComments(deliveryComments);
//
//        // Freeze the amount for the delivery days
//        User user = subscription.getUser();
//        BigDecimal totalAmountToFreeze = BigDecimal.ZERO;
//        for (LocalDate deliveryDay : deliveryDays) {
//            // Assuming you have access to the product price
//            int discountedPrice = cartItemService.getDiscountedPrice(user.getId());
//
//            // Calculate the amount to be frozen (discounted price * 1 day)
//            BigDecimal amountToFreeze = BigDecimal.valueOf(discountedPrice);
//
//            // Freeze the amount for 1 day
//            walletService.freezeAmount(user, amountToFreeze, deliveryDays, subscription);
//
//            // Add the amount to the total amount to freeze
//            totalAmountToFreeze = totalAmountToFreeze.add(amountToFreeze);
//        }
//
//        subscription = subscriptionRepository.save(subscription);
//        // After successfully scheduling the delivery, record the delivery status
//        for (LocalDate deliveryDay : deliveryDays) {
//            recordDeliveryStatus(subscription.getId(), deliveryDay, DeliveryStatus.PENDING);
//        }
//
//        return subscription;
//    }


    @Override
    public Subscription scheduleDelivery(Long subscriptionId, Product product, List<LocalDate> deliveryDays, LocalTime deliveryTime, String deliveryComments, boolean extendDelivery) {
        // Retrieve the subscription from the database
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isEmpty()) {
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
        Subscription subscription = optionalSubscription.get();

        // Extend delivery days only if the user wants
        if (extendDelivery) {
            subscription.setDeliveryDays(new ArrayList<>());
            subscription.getDeliveryDays().addAll(deliveryDays);
        } else {
            subscription.setDeliveryDays(deliveryDays);
        }

        subscription.setProduct(product);
        subscription.setDeliveryTime(deliveryTime);
        subscription.setDeliveryComments(deliveryComments);

        // Freeze the amount for the delivery days
        User user = subscription.getUser();
        BigDecimal totalAmountToFreeze = BigDecimal.ZERO;
        for (LocalDate deliveryDay : deliveryDays) {
            // Assuming you have access to the product price
            int discountedPrice = cartItemService.getDiscountedPrice(user.getId());
            // Calculate the amount to be frozen (discounted price * 1 day)
            BigDecimal amountToFreeze = BigDecimal.valueOf(discountedPrice);
            // Freeze the amount for 1 day
            walletService.freezeAmount(user, amountToFreeze, deliveryDays, subscription);
            // Add the amount to the total amount to freeze
            totalAmountToFreeze = totalAmountToFreeze.add(amountToFreeze);

            // Create order for each delivery day
            Order order = createOrderForDelivery(user, product, deliveryDay, discountedPrice);
            // Associate order with subscription
            order.setSubscription(subscription);
            // Save order
            orderRepository.save(order);
        }

        subscription = subscriptionRepository.save(subscription);

        // After successfully scheduling the delivery, record the delivery status
        for (LocalDate deliveryDay : deliveryDays) {
            recordDeliveryStatus(subscription.getId(), deliveryDay, DeliveryStatus.PENDING);
        }

        return subscription;
    }

    // Helper method to create an order for a specific delivery day
    private Order createOrderForDelivery(User user, Product product, LocalDate deliveryDay, int discountedPrice) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(deliveryDay.atStartOfDay());
        order.setOrderStatus(OrderStatus.PENDING); // Or any appropriate status
        order.getPaymentDetails().setStatus(PaymentStatus.PENDING); // Or any appropriate status
        order.setCreatedAt(LocalDateTime.now());
        // Assuming you have the logic to calculate total price, total discounted price, etc.
        // Set other order details accordingly
        order.setTotalPrice(discountedPrice); // Assuming each order has only one product
        order.setTotalDiscountedPrice(discountedPrice);
        // Add the product to order items
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(1); // Assuming only one quantity for simplicity
        orderItem.setPrice(discountedPrice);
        order.getOrderItems().add(orderItem);
        // Save order
        return order;
    }

    @Override
    public Subscription getActiveSubscriptionForUser(User user) {
        List<Subscription> activeSubscriptions = subscriptionRepository.findActiveSubscriptionsByUser(user);
        if (!activeSubscriptions.isEmpty()) {
            Subscription activeSubscription = activeSubscriptions.get(0);
            // Check if the active subscription has ended
            if (activeSubscription.getEndDate().isBefore(LocalDate.now())) {
                // Release any expired freeze amounts after the subscription ends
                walletService.releaseExpiredFreezeAmounts();
            }
            return activeSubscription;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void cancelSubscription(Long subscriptionId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            User user = subscription.getUser();

            // Manually delete associated freeze_amount records
            freezeAmountRepository.deleteBySubscription(subscription);

            // Calculate the amount to refund based on undelivered deliveries
            BigDecimal refundAmount = calculateRefundAmount(subscription);

            // Deduct cancellation fee from the refund amount
            BigDecimal cancellationFee = BigDecimal.valueOf(100);
            BigDecimal totalRefund = refundAmount.subtract(cancellationFee);

            // Check if the refund amount is negative (less than the cancellation fee)
            if (totalRefund.compareTo(BigDecimal.ZERO) < 0) {
                totalRefund = BigDecimal.ZERO; // Set refund amount to zero if negative
            }

            // Refund the total amount
            walletService.releaseFrozenAmount(user, totalRefund);

            // Deduct cancellation fee from the user's balance
            deductCancellationFee(user, cancellationFee);

            // Delete the subscription
            subscriptionRepository.delete(subscription);
        } else {
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
    }

    private BigDecimal calculateRefundAmount(Subscription subscription) {
        BigDecimal totalRefundAmount = BigDecimal.ZERO;

        // Get the delivery days associated with the subscription
        List<LocalDate> deliveryDays = subscription.getDeliveryDays();

        // Calculate the number of undelivered deliveries
        int undeliveredDeliveries = 0;
        LocalDate currentDate = LocalDate.now();
        for (LocalDate deliveryDate : deliveryDays) {
            if (!deliveryService.isDeliveryMade(deliveryDate)) {
                undeliveredDeliveries++;
            }
        }

        // Assuming you have access to the product price
        int discountedPrice = cartItemService.getDiscountedPrice(subscription.getUser().getId());

        // Calculate the total refund amount for undelivered deliveries
        totalRefundAmount = BigDecimal.valueOf(discountedPrice)
                .multiply(BigDecimal.valueOf(undeliveredDeliveries));

        return totalRefundAmount;
    }

    @Override
    public void deductCancellationFee(User user, BigDecimal cancellationFee) {
        // Retrieve the user's wallet
        Wallet userWallet = user.getWallet();

        // Deduct the cancellation fee from the user's balance
        BigDecimal currentBalance = userWallet.getBalance();
        BigDecimal updatedBalance = currentBalance;
        // Check if the balance is negative after deduction
        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            updatedBalance = BigDecimal.ZERO; // Set balance to zero if negative
        }
        userWallet.setBalance(updatedBalance);

        // Save the updated wallet
        walletRepository.save(userWallet);

        // Create a transaction entry to reflect the deduction of the cancellation fee
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.CANCELLATION_FEE);
        transaction.setAmount(cancellationFee.negate()); // Negate the amount to indicate deduction
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public Subscription extendSubscription(Long subscriptionId, LocalDate newEndDate) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isEmpty()) {
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
        Subscription subscription = optionalSubscription.get();

        // Update the end date of the subscription
        subscription.setEndDate(newEndDate);
        // Update the expiry date of freeze amounts associated with the subscription
        List<FreezeAmount> freezeAmounts = freezeAmountRepository.findBySubscription(subscription);
        for (FreezeAmount freezeAmount : freezeAmounts) {
            freezeAmount.setExpiryDate(newEndDate);
            freezeAmountRepository.save(freezeAmount);
        }


        // Save and return the updated subscription
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription pauseSubscription(Long subscriptionId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            subscription.setPaused(true);
            return subscriptionRepository.save(subscription);
        } else {
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
    }

//    @Override
//    public Subscription resumeSubscription(Long subscriptionId) {
//        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
//        if (optionalSubscription.isPresent()) {
//            Subscription subscription = optionalSubscription.get();
//            subscription.setPaused(false);
//
//            // If the subscription end date is today, extend the subscription by 30 days
//            if (subscription.getEndDate().isEqual(LocalDate.now())) {
//                LocalDate newEndDate = LocalDate.now().plusDays(30);
//                subscription.setEndDate(newEndDate);
//            }
//
//            // Check if today's delivery has already occurred
//            LocalDate today = LocalDate.now();
//            LocalTime currentTime = LocalTime.now();
//            LocalDate nextDeliveryDay = subscription.getDeliveryDays().stream()
//                    .filter(deliveryDay -> deliveryDay.isAfter(today) || (deliveryDay.isEqual(today) && currentTime.isBefore(LocalTime.of(12, 0))))
//                    .findFirst()
//                    .orElse(today.plusDays(1));
//
//            // Set the next delivery day
//            subscription.setDeliveryDay(nextDeliveryDay);
//
//            // Save and return the updated subscription
//            return subscriptionRepository.save(subscription);
//        } else {
//            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
//        }
//    }

    @Override
    public Subscription resumeSubscription(Long subscriptionId) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            subscription.setPaused(false);

            LocalDate today = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // Remove dates <= today
            List<LocalDate> deliveryDays = subscription.getDeliveryDays();
            List<LocalDate> updatedDeliveryDays = new ArrayList<>();
            int removedCount = 0;

            for (LocalDate deliveryDay : deliveryDays) {
                if (deliveryDay.isAfter(today)) {
                    updatedDeliveryDays.add(deliveryDay);
                } else {
                    removedCount++;
                }
            }

            // Get the last date in the updated delivery days list
            LocalDate lastDeliveryDate = updatedDeliveryDays.get(updatedDeliveryDays.size() - 1);

            // Add the same number of dates to the end of the array
            for (int i = 1; i <= removedCount; i++) {
                lastDeliveryDate = lastDeliveryDate.plusDays(1);
                updatedDeliveryDays.add(lastDeliveryDate);
            }

            // Update the subscription's delivery days
            subscription.setDeliveryDays(updatedDeliveryDays);
            // If subscription type is "custom," modify the end date based on start date
            if (subscription.getType() == SubscriptionType.CUSTOM) {
                // Assuming custom duration is 30 days
                LocalDate newEndDate = subscription.getStartDate().plusDays(30);
                subscription.setEndDate(newEndDate);
            }else{
                subscription.setEndDate(lastDeliveryDate);
            }

            // Save and return the updated subscription
            return subscriptionRepository.save(subscription);
        } else {
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
    }


    @Override
    public void recordDeliveryStatus(Long subscriptionId, LocalDate deliveryDate, DeliveryStatus status) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            // Create a new Delivery entity
            Delivery delivery = new Delivery();
            delivery.setSubscription(subscription);
            delivery.setDeliveryDate(deliveryDate);
            delivery.setStatus(status);
            // Save the delivery
            deliveryService.recordDelivery(delivery);
        } else {
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
    }

    public void updateDeliveryStatus(Long subscriptionId, LocalDate deliveryDate, DeliveryStatus status) {
        // Fetch the subscription from the database
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            // Check if the delivery date is valid (part of the subscription's delivery days)
            if (subscription.getDeliveryDays().contains(deliveryDate)) {
                // Assuming there's a method to check if delivery was made on the given date
                boolean deliveryMade = deliveryService.isDeliveryMade(deliveryDate);
                if (deliveryMade) {
                    // Update the delivery status
                    recordDeliveryStatus(subscriptionId, deliveryDate, status);
                    // If the status is delivered, deduct payment from the freeze table
                    if (status == DeliveryStatus.DELIVERED) {
                        deductPaymentFromFreezeTable(subscription, deliveryDate);
                    }
                } else {
                    // If delivery wasn't made, you may want to handle this case accordingly
                    // For example, throw an exception, log a warning, etc.
                    // Here, I'm throwing an exception for demonstration purposes
                    throw new IllegalStateException("Delivery was not made on the specified date: " + deliveryDate);
                }
            } else {
                // If the delivery date is not valid, throw an exception or handle it accordingly
                throw new IllegalArgumentException("Invalid delivery date for the subscription: " + deliveryDate);
            }
        } else {
            // If the subscription is not found, throw an exception or handle it accordingly
            throw new NotFoundException("Subscription not found with ID: " + subscriptionId);
        }
    }

    private void deductPaymentFromFreezeTable(Subscription subscription, LocalDate deliveryDate) {
        // Retrieve the freeze amounts associated with the subscription and delivery date
        List<FreezeAmount> freezeAmounts = freezeAmountRepository.findBySubscriptionAndExpiryDate(subscription, deliveryDate);

        // Deduct payment for each freeze amount
        for (FreezeAmount freezeAmount : freezeAmounts) {
            // Deduct the amount from the user's wallet
            Wallet userWallet = freezeAmount.getUser().getWallet();
            BigDecimal currentBalance = userWallet.getBalance();
            BigDecimal updatedBalance = currentBalance.subtract(freezeAmount.getAmount());
            userWallet.setBalance(updatedBalance);
            walletRepository.save(userWallet);

            // Delete the freeze amount
            freezeAmountRepository.delete(freezeAmount);
        }
    }
}
