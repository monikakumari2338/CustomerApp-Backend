package com.deepanshu.repository;

import com.deepanshu.modal.Delivery;
import com.deepanshu.user.domain.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    boolean existsByDeliveryDateAndStatus(LocalDate deliveryDate, DeliveryStatus status);
}