package com.deepanshu.repository;

import com.deepanshu.modal.Subscription;
import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findActiveSubscriptionsByUser(User user);
}
