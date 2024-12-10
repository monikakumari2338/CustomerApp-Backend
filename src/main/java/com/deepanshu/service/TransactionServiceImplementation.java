package com.deepanshu.service;

import com.deepanshu.exception.InsufficientBalanceException;
import com.deepanshu.modal.Transaction;
import com.deepanshu.modal.User;
import com.deepanshu.modal.Wallet;
import com.deepanshu.repository.TransactionRepository;
import com.deepanshu.repository.WalletRepository;
import com.deepanshu.user.domain.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TransactionServiceImplementation implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Transaction addMoney(User user, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.ADD_MONEY);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        walletRepository.save(wallet);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public Transaction sendMoney(User sender, User receiver, BigDecimal amount) {
        Wallet senderWallet = sender.getWallet();
        BigDecimal senderBalance = senderWallet.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Sender does not have sufficient balance");
        }

        BigDecimal updatedSenderBalance = senderBalance.subtract(amount);
        senderWallet.setBalance(updatedSenderBalance);

        Wallet receiverWallet = receiver.getWallet();
        BigDecimal receiverBalance = receiverWallet.getBalance();
        BigDecimal updatedReceiverBalance = receiverBalance.add(amount);
        receiverWallet.setBalance(updatedReceiverBalance);

        Transaction senderTransaction = new Transaction();
        senderTransaction.setUser(sender);
        senderTransaction.setTransactionType(TransactionType.SEND_MONEY);
        senderTransaction.setAmount(amount);
        senderTransaction.setTransactionDate(LocalDateTime.now());

        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setUser(receiver);
        receiverTransaction.setTransactionType(TransactionType.RECEIVE_MONEY);
        receiverTransaction.setAmount(amount);
        receiverTransaction.setTransactionDate(LocalDateTime.now());

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        return senderTransaction;
    }


    @Override
    public List<Transaction> getWalletHistory(User user) {
        return transactionRepository.findByUserOrderByTransactionDateDesc(user);
    }

    @Override
    public List<Transaction> getRequestStatement(User user, LocalDate fromDate, LocalDate toDate) {
        return transactionRepository.findByUserAndTransactionDateBetweenOrderByTransactionDateDesc(user, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
    }
}
