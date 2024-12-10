package com.deepanshu.service;

import com.deepanshu.modal.Product;
import com.deepanshu.modal.Subscription;
import com.deepanshu.modal.User;
import com.deepanshu.user.domain.DeliveryStatus;
import com.deepanshu.user.domain.SubscriptionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SubscriptionService {
    Subscription createSubscription(User user, SubscriptionType subcriptionType, LocalDate startDate, LocalDate endDate);

    Subscription scheduleDelivery(Long subscriptionId, Product product, List<LocalDate> deliveryDays, LocalTime deliveryTime, String deliveryComments, boolean extendDelivery);

    Subscription getActiveSubscriptionForUser(User user);

    Subscription extendSubscription(Long subscriptionId, LocalDate newEndDate);

    void cancelSubscription(Long subscriptionId);
    void deductCancellationFee(User user, BigDecimal cancellationFee);

    Subscription pauseSubscription(Long subscriptionId);
    Subscription resumeSubscription(Long subscriptionId);

    void recordDeliveryStatus(Long subscriptionId, LocalDate deliveryDate, DeliveryStatus status);

    void updateDeliveryStatus(Long subscriptionId, LocalDate deliveryDate, DeliveryStatus status);
}
