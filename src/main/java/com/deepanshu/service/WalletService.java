package com.deepanshu.service;

import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Subscription;
import com.deepanshu.modal.Transaction;
import com.deepanshu.modal.User;
import com.deepanshu.modal.Wallet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WalletService {
    Wallet getWalletByUser(User user);

    Wallet addMoney(User user, BigDecimal amount);
    Wallet sendMoney(User sender, User receiver, BigDecimal amount);
    List<Transaction> getWalletHistory(Long userId) throws UserException;
    Wallet freezeAmount(User user, BigDecimal amount, List<LocalDate> deliveryDays, Subscription subscription);
    void releaseExpiredFreezeAmounts();
    void releaseFrozenAmount(User user, BigDecimal amount);
    BigDecimal getTotalFrozenAmountForUser(User user);

    Wallet payForOrderWithWallet(User user, BigDecimal amount);
}
