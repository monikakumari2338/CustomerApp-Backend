package com.deepanshu.repository;

import com.deepanshu.modal.Reward;
import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByUserId(Long userId);

    List<Reward> findByUser(User user);

    List<Reward> findByUserIdAndEarnedPointsGreaterThan(Long userId, int earnedPoints);

    List<Reward> findByUserIdAndUsedPointsGreaterThanAndRedeemed(Long userId, int usedPoints, boolean redeemed);


}
