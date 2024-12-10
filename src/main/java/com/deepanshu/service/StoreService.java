package com.deepanshu.service;

import com.deepanshu.modal.Store;

import java.util.List;

public interface StoreService {
    List<Store> findStoresByCity(String city);

    List<Store> findStoresByPincode(String pincode);
}
