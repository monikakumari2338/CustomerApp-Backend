package com.deepanshu.service;

import com.deepanshu.modal.Store;
import com.deepanshu.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImplementation implements StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<Store> findStoresByCity(String city) {
        return storeRepository.findByCity(city);
    }

    @Override
    public List<Store> findStoresByPincode(String pincode) {
        return storeRepository.findByPincode(pincode);
    }
}
