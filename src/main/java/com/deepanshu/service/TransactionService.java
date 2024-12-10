package com.deepanshu.service;

import com.deepanshu.modal.Transaction;
import com.deepanshu.modal.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    Transaction addMoney(User user, BigDecimal amount);

    Transaction sendMoney(User sender, User receiver, BigDecimal amount);

    List<Transaction> getWalletHistory(User user);

    List<Transaction> getRequestStatement(User user, LocalDate fromDate, LocalDate toDate);
}
