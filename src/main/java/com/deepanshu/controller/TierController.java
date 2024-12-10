package com.deepanshu.controller;

import com.deepanshu.modal.Tier;
import com.deepanshu.service.TierService;
import com.deepanshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tiers")
@CrossOrigin(origins = "https://localhost:8081")
public class TierController {

    private final TierService tierService;
    private final UserService userService;

    @Autowired
    public TierController(TierService tierService, UserService userService) {
        this.tierService = tierService;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Tier> getTierByUserId(@PathVariable Long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        Tier tier = tierService.determineTier(userId);

        if (tier != null) {
            return ResponseEntity.ok(tier);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
