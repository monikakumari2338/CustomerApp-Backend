package com.deepanshu.service;

import com.deepanshu.modal.*;
import com.deepanshu.repository.OrderRepository;
import com.deepanshu.repository.RewardRepository;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.user.domain.RewardStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RewardServiceImplementation implements RewardService {
    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private int redeemedPointsInCurrentTransaction = 0;

    public RewardServiceImplementation(RewardRepository rewardRepository, UserRepository userRepository, CartItemService cartItemService, OrderRepository orderRepository) {
        this.rewardRepository = rewardRepository;
        this.userRepository = userRepository;
        this.cartItemService = cartItemService;
        this.orderRepository = orderRepository;
    }

    public void earnReward(User user, int points) {
        Reward reward = new Reward();
        reward.setUser(user);
        reward.setPoints(points);
        reward.setEarnedPoints(points);
        reward.setStatus(RewardStatus.Earn_On_Order);
        rewardRepository.save(reward);
    }

    @Override
    public List<Reward> getRewardsByUser(Long userId) {
        return rewardRepository.findByUserId(userId);
    }

    @Override
    public void redeemPoints(User user, int pointsToRedeem) {
        List<Reward> userRewards = rewardRepository.findByUser(user);
        int totalPoints = userRewards.stream().mapToInt(Reward::getPoints).sum();

        if (totalPoints >= pointsToRedeem) {
            int remainingPoints = pointsToRedeem;
            LocalDateTime currentTime = LocalDateTime.now();

            while (remainingPoints > 0) {
                Reward newReward = new Reward();
                newReward.setUser(user);
                int pointsInReward = Math.min(remainingPoints, 100);

                newReward.setPoints(-pointsInReward);
                newReward.setUsedPoints(pointsInReward);
                redeemedPointsInCurrentTransaction = pointsInReward;
                newReward.setRedeemed(true);
                newReward.setCreditDate(currentTime);

                List<Integer> redeemedPointsHistory = new ArrayList<>();
                redeemedPointsHistory.add(pointsInReward);
                newReward.setRedeemedPointsHistory(redeemedPointsHistory);

                rewardRepository.save(newReward);

                remainingPoints -= pointsInReward;
                redeemedPointsInCurrentTransaction = pointsInReward;
                newReward.setStatus(RewardStatus.Redeemed);
            }

            cartItemService.updateDiscountedPriceInCartItems(user.getId(), pointsToRedeem);
        } else {
            throw new IllegalArgumentException("Insufficient points to redeem");
        }
    }

    @Override
    public int calculateRedeemedPoints(User user) {
        int redeemedPoints = 0;
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Reward> userRewards = rewardRepository.findByUser(user);
        for (Reward reward : userRewards) {
            if (reward.getCreditDate().isAfter(currentDateTime.minusMonths(3))) {
                redeemedPoints += reward.getPoints();
            }
        }
        return redeemedPoints;
    }

    @Override
    public List<Reward> getEarnedRewardsByUser(Long userId) {
        return rewardRepository.findByUserIdAndEarnedPointsGreaterThan(userId, 0);
    }

    @Override
    public List<Reward> getUsedRewardsByUser(Long userId) {
        return rewardRepository.findByUserIdAndUsedPointsGreaterThanAndRedeemed(userId, 0, true);
    }

    @Override
    public void revertPoints(User user, int pointsToRevert) {
        List<Reward> userRewards = rewardRepository.findByUser(user);
        int totalPoints = 0;
        for (Reward reward : userRewards) {
            totalPoints += reward.getPoints();
        }

        if (totalPoints >= pointsToRevert) {
            int remainingPoints = pointsToRevert;
            for (Reward reward : userRewards) {
                if (remainingPoints > 0) {
                    int pointsInReward = reward.getPoints();
                    if (pointsInReward >= remainingPoints) {
                        reward.setPoints(pointsInReward + remainingPoints);
                        reward.setUsedPoints(reward.getUsedPoints() - remainingPoints);
                        remainingPoints = 0;
                    } else {
                        remainingPoints -= pointsInReward;
                        reward.setUsedPoints(reward.getUsedPoints() - pointsInReward);
                        reward.setPoints(0);
                    }
                    rewardRepository.save(reward);
                } else {
                    break;
                }
            }
            cartItemService.updateCartValue(user.getId(), pointsToRevert);
        } else {
            throw new IllegalArgumentException("Insufficient points to revert");
        }
    }

    @Override
    public void saveReward(Reward reward) {
        rewardRepository.save(reward);
    }

    @Override
    public int calculateRedeemedPointsagain(User user) {
        int redeemedPoints = redeemedPointsInCurrentTransaction;
        redeemedPointsInCurrentTransaction = 0;
        return redeemedPoints;
    }

    @Override
    public void earnOnSignUp(User user) {
        Reward reward = new Reward();
        reward.setUser(user);
        reward.setPoints(200);
        reward.setEarnedPoints(200);
        reward.setStatus(RewardStatus.Earn_On_SignUp);
        reward.setCreditDate(LocalDateTime.now());
        rewardRepository.save(reward);
    }

    @Override
    public void earnOnReferral(User referrer, User referee) {
        Reward rewardReferrer = new Reward();
        rewardReferrer.setUser(referrer);
        rewardReferrer.setPoints(100);
        rewardReferrer.setEarnedPoints(100);
        rewardReferrer.setStatus(RewardStatus.Earn_On_Referral);
        rewardReferrer.setCreditDate(LocalDateTime.now());
        rewardRepository.save(rewardReferrer);

        Reward rewardReferee = new Reward();
        rewardReferee.setUser(referee);
        rewardReferee.setPoints(100);
        rewardReferee.setEarnedPoints(100);
        rewardReferee.setStatus(RewardStatus.Earn_On_Referral);
        rewardReferee.setCreditDate(LocalDateTime.now());
        rewardRepository.save(rewardReferee);
    }
}

