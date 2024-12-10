package com.deepanshu.repository;

import com.deepanshu.modal.FreezeAmount;
import com.deepanshu.modal.Subscription;
import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FreezeAmountRepository extends JpaRepository<FreezeAmount,Long> {
    @Query("SELECT SUM(amount) FROM FreezeAmount f WHERE f.user = ?1")
    BigDecimal getTotalFrozenAmountForUser(User sender);

    List<FreezeAmount> findByExpiryDateBefore(LocalDate now);

    List<FreezeAmount> findByUser(User user);

    List<FreezeAmount> findByUserAndExpiryDateAfter(User user, LocalDate now);

    List<FreezeAmount> findByUserAndSubscription(User user, Subscription subscription);

    List<FreezeAmount> findBySubscription(Subscription subscription);

    void deleteBySubscription(Subscription subscription);

    List<FreezeAmount> findBySubscriptionAndExpiryDate(Subscription subscription, LocalDate deliveryDate);
}
