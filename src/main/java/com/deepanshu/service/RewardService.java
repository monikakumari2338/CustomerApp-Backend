package com.deepanshu.service;

import com.deepanshu.modal.Cart;
import com.deepanshu.modal.Order;
import com.deepanshu.modal.Reward;
import com.deepanshu.modal.User;

import java.util.List;

public interface RewardService {

    void earnReward(User user, int points);

    List<Reward> getRewardsByUser(Long userId);

    void redeemPoints(User user, int points);

    int calculateRedeemedPoints(User user);

    List<Reward> getEarnedRewardsByUser(Long userId);

    List<Reward> getUsedRewardsByUser(Long userId);

    void revertPoints(User user, int points);

    void saveReward(Reward reward);

    int calculateRedeemedPointsagain(User user);

    void earnOnSignUp(User user);

    void earnOnReferral(User referrer, User referee);
}
