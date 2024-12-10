package com.deepanshu.repository;

import com.deepanshu.modal.Transaction;
import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByTransactionDateDesc(User user);

    List<Transaction> findByUserAndTransactionDateBetweenOrderByTransactionDateDesc(User user, LocalDateTime fromDate, LocalDateTime toDate);
}

