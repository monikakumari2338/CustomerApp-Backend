package com.deepanshu.repository;

import com.deepanshu.modal.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByCity(String city);

    List<Store> findByPincode(String pincode);
}
