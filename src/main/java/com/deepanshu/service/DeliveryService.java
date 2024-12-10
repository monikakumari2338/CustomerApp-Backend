package com.deepanshu.service;

import com.deepanshu.modal.Delivery;

import java.time.LocalDate;

public interface DeliveryService {
    boolean isDeliveryMade(LocalDate deliveryDate);

    void recordDelivery(Delivery delivery);
}
