package com.deepanshu.controller;

import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Reward;
import com.deepanshu.modal.User;
import com.deepanshu.repository.RewardRepository;
import com.deepanshu.service.RewardService;
import com.deepanshu.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@CrossOrigin(origins = "https://localhost:8081")
public class RewardController {

    private final RewardService rewardService;
    private final UserService userService;

    private final RewardRepository rewardRepository;

    public RewardController(RewardService rewardService, UserService userService, RewardRepository rewardRepository) {
        this.rewardService = rewardService;
        this.userService = userService;
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/user/{userId}/rewards")
    public ResponseEntity<List<Reward>> getRewardsByUser(@PathVariable("userId") Long userId) {
        List<Reward> rewards = rewardService.getRewardsByUser(userId);
        return ResponseEntity.ok(rewards);
    }

    @PostMapping("/redeem")
    public ResponseEntity<Integer> redeemPoints(@RequestParam("userId") Long userId, @RequestParam("points") int points) throws UserException {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        rewardService.redeemPoints(user, points);
        return ResponseEntity.ok(points);
    }


    @GetMapping("/user/{userId}/redeemed-points")
    public ResponseEntity<Integer> calculateRedeemedPoints(@PathVariable("userId") Long userId) {
        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            int redeemedPoints = rewardService.calculateRedeemedPoints(user);
            return ResponseEntity.ok(redeemedPoints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/{userId}/earned-points")
    public ResponseEntity<List<Reward>> getEarnedPointsByUser(@PathVariable("userId") Long userId) {
        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<Reward> earnedRewards = rewardService.getEarnedRewardsByUser(userId);
            for (Reward reward : earnedRewards) {
                List<Reward> userRewards = rewardRepository.findByUser(user);
                List<Integer> redeemedPointsHistory = new ArrayList<>();
                int totalRedeemedPoints = 0;
                for (Reward userReward : userRewards) {
                    totalRedeemedPoints += userReward.getUsedPoints();
                    redeemedPointsHistory.add(totalRedeemedPoints);
                }
                reward.setRedeemedPointsHistory(redeemedPointsHistory);
            }

            return ResponseEntity.ok(earnedRewards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/user/{userId}/used-points")
    public ResponseEntity<List<Reward>> getUsedPointsByUser(@PathVariable("userId") Long userId) {
        List<Reward> usedPoints = rewardService.getUsedRewardsByUser(userId);
        return ResponseEntity.ok(usedPoints);
    }

    @PostMapping("/revert")
    public ResponseEntity<Void> revertPoints(@RequestParam("userId") Long userId, @RequestParam("points") int points) {
        try {
            User user = userService.findUserById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            rewardService.revertPoints(user, points);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
