package com.deepanshu.service;

import com.deepanshu.modal.Reward;
import com.deepanshu.modal.Tier;
import com.deepanshu.modal.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TierServiceImplementation implements TierService {

    private RewardService rewardService;

    public TierServiceImplementation(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @Override
    public Tier determineTier(Long userId) {
        List<Reward> rewards = rewardService.getRewardsByUser(userId);
        int totalEarnedPoints = rewards.stream().mapToInt(Reward::getEarnedPoints).sum();

        if (totalEarnedPoints >= 800) {
            return Tier.PLATINUM;
        } else if (totalEarnedPoints >= 500) {
            return Tier.GOLD;
        } else if (totalEarnedPoints >= 200) {
            return Tier.SILVER;
        } else {
            return Tier.REGULAR;
        }
    }

    @Override
    public void calculateMembershipStatus(User user, Tier tier) {
        LocalDate currentDate = LocalDate.now();
        LocalDate membershipStartDate = user.getMembershipStartDate();

        if (membershipStartDate == null || membershipStartDate.isBefore(currentDate.minusYears(1))) {
            user.setMembershipStartDate(currentDate);
            user.setMembershipActive(true);
        } else {
            user.setMembershipActive(true);
        }
    }

}
