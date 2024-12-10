package com.deepanshu.service;

import com.deepanshu.modal.StorePickup;
import com.deepanshu.modal.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StorePickupService {
    List<Object[]> findPickupDateTimesByStoreId(Long storeId);

    void savePickupDateTime(Long storeId, List<Long> productIds, List<String> sizeNames, List<Integer> quantities, LocalDateTime pickupDateTime, String comment, Long userId);

    StorePickup getActiveStorePickupForUser(User user);

    void updatePickupDateTime(Long pickupId, LocalDateTime newPickupDateTime, String newComment);

    Optional<StorePickup> getStorePickupById(Long id);
}
