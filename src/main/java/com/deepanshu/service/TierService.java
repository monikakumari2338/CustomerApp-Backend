package com.deepanshu.service;

import com.deepanshu.modal.Reward;
import com.deepanshu.modal.Tier;
import com.deepanshu.modal.User;

import java.util.List;

public interface TierService {


    Tier determineTier(Long userId);

    void calculateMembershipStatus(User user, Tier tier);
}
