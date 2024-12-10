package com.deepanshu.service;

import com.deepanshu.exception.InsufficientBalanceException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.*;
import com.deepanshu.repository.FreezeAmountRepository;
import com.deepanshu.repository.TransactionRepository;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.repository.WalletRepository;
import com.deepanshu.user.domain.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletServiceImplementation implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FreezeAmountRepository freezeAmountRepository;


    @Override
    public Wallet getWalletByUser(User user) {
        return user.getWallet();
    }

    @Override
    public Wallet addMoney(User user, BigDecimal amount) {
        Wallet wallet = user.getWallet();
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
        }
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.ADD_MONEY);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        return wallet;
    }

    @Override
    public Wallet sendMoney(User sender, User receiver, BigDecimal amount) {
        Wallet senderWallet = sender.getWallet();
        if (senderWallet == null) {
            senderWallet = new Wallet();
            senderWallet.setUser(sender);
            sender.setWallet(senderWallet);
        }

        // Check if sender has any frozen amount
        BigDecimal frozenAmount = freezeAmountRepository.getTotalFrozenAmountForUser(sender);
        BigDecimal senderBalance = senderWallet.getBalance().subtract(frozenAmount);
        if (senderBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Sender does not have sufficient balance");
        }
        BigDecimal updatedSenderBalance = senderBalance.subtract(amount);
        senderWallet.setBalance(updatedSenderBalance);

        Wallet receiverWallet = receiver.getWallet();
        if (receiverWallet == null) {
            receiverWallet = new Wallet();
            receiverWallet.setUser(receiver);
            receiver.setWallet(receiverWallet);
        }
        BigDecimal receiverBalance = receiverWallet.getBalance();
        BigDecimal updatedReceiverBalance = receiverBalance.add(amount);
        receiverWallet.setBalance(updatedReceiverBalance);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

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

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        if (updatedSenderBalance.compareTo(BigDecimal.valueOf(1000)) < 0) {
            BigDecimal bonusAmount = BigDecimal.valueOf(1000);
            senderWallet.setBalance(updatedSenderBalance.add(bonusAmount));
            walletRepository.save(senderWallet);

            Transaction bonusTransaction = new Transaction();
            bonusTransaction.setUser(sender);
            bonusTransaction.setTransactionType(TransactionType.BONUS);
            bonusTransaction.setAmount(bonusAmount);
            bonusTransaction.setTransactionDate(LocalDateTime.now());
            transactionRepository.save(bonusTransaction);
        }
        return senderWallet;
    }

    @Override
    public List<Transaction> getWalletHistory(Long userId) throws UserException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with ID: " + userId));
        return transactionRepository.findByUserOrderByTransactionDateDesc(user);
    }

    @Override
    public Wallet freezeAmount(User user, BigDecimal amount, List<LocalDate> deliveryDays, Subscription subscription) {
        // Check if freeze amount already exists for the subscription
        List<FreezeAmount> existingFreezeAmounts = freezeAmountRepository.findByUserAndSubscription(user, subscription);
        if (!existingFreezeAmounts.isEmpty()) {
            // Freeze amount already exists, no need to freeze again
            return user.getWallet();
        }
        LocalDate expiryDate=subscription.getEndDate();

        for (LocalDate deliveryDay : deliveryDays) {
            // Create freeze amount entity
            FreezeAmount freezeAmount = new FreezeAmount();
            freezeAmount.setUser(user);
            freezeAmount.setAmount(amount);
            freezeAmount.setExpiryDate(expiryDate);
            freezeAmount.setSubscription(subscription);

            // Save freeze amount to the database
            freezeAmountRepository.save(freezeAmount);

            // You may implement logic to update user's wallet balance accordingly

            // Update user's wallet balance to reflect the frozen amount
            Wallet userWallet = user.getWallet();
            BigDecimal currentBalance = userWallet.getBalance();
            BigDecimal updatedBalance = currentBalance.subtract(amount);
            userWallet.setBalance(updatedBalance);
            walletRepository.save(userWallet);

            // Create a transaction entry to reflect the frozen amount
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setTransactionType(TransactionType.FREEZE_AMOUNT);
            transaction.setAmount(amount.negate()); // Negative amount to indicate freezing
            transaction.setTransactionDate(LocalDateTime.now());
            transactionRepository.save(transaction);
        }

        return user.getWallet();
    }
    @Scheduled(fixedRate = 86400000) // Runs every day (24 hours)
    public void releaseExpiredFreezeAmounts() {
        List<FreezeAmount> expiredFreezeAmounts = freezeAmountRepository.findByExpiryDateBefore(LocalDate.now());
        for (FreezeAmount freezeAmount : expiredFreezeAmounts) {
            // Release the frozen amount by updating the user's wallet balance
            Wallet userWallet = freezeAmount.getUser().getWallet();
            BigDecimal currentBalance = userWallet.getBalance();
            BigDecimal releasedAmount = freezeAmount.getAmount();
            BigDecimal updatedBalance = currentBalance.add(releasedAmount);
            userWallet.setBalance(updatedBalance);
            walletRepository.save(userWallet);

            // Remove the expired freeze amount from the database
            freezeAmountRepository.delete(freezeAmount);
        }
    }

    @Override
    public void releaseFrozenAmount(User user, BigDecimal amount) {
        // Retrieve the user's wallet
        Wallet userWallet = user.getWallet();

        // Update the wallet balance to release the frozen amount
        BigDecimal currentBalance = userWallet.getBalance();
        BigDecimal updatedBalance = currentBalance.add(amount);
        userWallet.setBalance(updatedBalance);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.REFUND_AMOUNT);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Save the updated wallet
        walletRepository.save(userWallet);
    }
    @Override
    public BigDecimal getTotalFrozenAmountForUser(User user) {
        return freezeAmountRepository.getTotalFrozenAmountForUser(user);
    }

    @Override
    public Wallet payForOrderWithWallet(User user, BigDecimal amount) {
        Wallet userWallet = user.getWallet();
        BigDecimal currentBalance = userWallet.getBalance();

        if (currentBalance.compareTo(amount) < 0) {
            // Handle insufficient balance error
            throw new InsufficientBalanceException("Insufficient balance to pay for the order");
        }

        // Deduct the amount from the user's wallet
        BigDecimal updatedBalance = currentBalance.subtract(amount);
        userWallet.setBalance(updatedBalance);
        walletRepository.save(userWallet);

        // Create a transaction entry to reflect the payment
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType(TransactionType.ORDER_PAYMENT);
        transaction.setAmount(amount.negate()); // Negative amount to indicate payment
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return userWallet;
    }



}
