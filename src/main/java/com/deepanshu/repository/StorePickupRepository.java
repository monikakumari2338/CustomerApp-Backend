package com.deepanshu.repository;

import com.deepanshu.modal.StorePickup;
import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePickupRepository extends JpaRepository<StorePickup, Long> {
    List<StorePickup> findAllByStoreId(Long storeId);

    List<StorePickup> findActiveStorePickupByUser(User user);
}

