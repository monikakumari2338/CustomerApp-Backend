package com.deepanshu.controller;

import com.deepanshu.modal.StorePickup;
import com.deepanshu.request.PickupRequest;
import com.deepanshu.service.StorePickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/store-pickups")
@CrossOrigin(origins = "https://localhost:8081")
public class StorePickupController {

    @Autowired
    private StorePickupService pickupService;

    @GetMapping("/store/{storeId}")
    public List<Object[]> getPickupsByStoreId(@PathVariable Long storeId) {
        return pickupService.findPickupDateTimesByStoreId(storeId);
    }

    @PostMapping("/{storeId}/pickups/comment")
    public ResponseEntity<String> selectPickupDateTime(
            @PathVariable Long storeId,
            @RequestBody PickupRequest pickupRequest
    ) {
        pickupService.savePickupDateTime(
                storeId,
                pickupRequest.getProductIds(),
                pickupRequest.getSizeNames(),
                pickupRequest.getQuantities(),
                pickupRequest.getPickupDateTime(),
                pickupRequest.getComment(),
                pickupRequest.getUserId()
        );
        return new ResponseEntity<>("Pickup date time and comment saved successfully", HttpStatus.CREATED);
    }


    @PutMapping("/reschedule/{pickupId}")
    public ResponseEntity<String> reschedulePickup(
            @PathVariable Long pickupId,
            @RequestParam("newPickupDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newPickupDateTime,
            @RequestParam("newComment") String newComment
    ) {
        pickupService.updatePickupDateTime(pickupId, newPickupDateTime, newComment);
        return new ResponseEntity<>("Pickup rescheduled successfully", HttpStatus.OK);
    }
}
