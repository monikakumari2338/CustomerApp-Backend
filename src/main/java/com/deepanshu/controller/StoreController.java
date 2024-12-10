package com.deepanshu.controller;

import com.deepanshu.modal.Store;
import com.deepanshu.repository.StoreRepository;
import com.deepanshu.service.DistanceCalculatorService;
import com.deepanshu.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@CrossOrigin(origins = "https://localhost:8081")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private DistanceCalculatorService distanceCalculatorService;

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("/city/{city}")
    public List<Store> getStoresByCity(@PathVariable String city) {
        return storeService.findStoresByCity(city);
    }

    @GetMapping("/pincode/{pincode}")
    public List<Store> getStoresByPincode(@PathVariable String pincode) {
        return storeService.findStoresByPincode(pincode);
    }

    @GetMapping("/distance")
    public List<Store> getStoresSortedByDistance(@RequestParam double userLat,@RequestParam double userLong)
    {
        List<Store> stores=storeRepository.findAll();
        stores.sort((s1, s2) -> {
            double distanceToS1 = distanceCalculatorService.calculateDistance(userLat, userLong, s1.getLatitude(), s1.getLongitude());
            double distanceToS2 = distanceCalculatorService.calculateDistance(userLat, userLong, s2.getLatitude(), s2.getLongitude());
            return Double.compare(distanceToS1, distanceToS2);
        });
        return stores;
    }
}
