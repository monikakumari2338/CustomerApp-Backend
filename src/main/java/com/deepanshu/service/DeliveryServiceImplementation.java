package com.deepanshu.service;

import com.deepanshu.modal.Delivery;
import com.deepanshu.modal.Subscription;
import com.deepanshu.repository.DeliveryRepository;
import com.deepanshu.user.domain.DeliveryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DeliveryServiceImplementation implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    public boolean isDeliveryMade(LocalDate deliveryDate) {
        return deliveryRepository.existsByDeliveryDateAndStatus(deliveryDate, DeliveryStatus.DELIVERED);
    }

    @Override
    public void recordDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
