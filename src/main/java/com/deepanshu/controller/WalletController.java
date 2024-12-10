package com.deepanshu.controller;

import com.deepanshu.exception.InsufficientBalanceException;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Transaction;
import com.deepanshu.modal.User;
import com.deepanshu.modal.Wallet;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.service.UserService;
import com.deepanshu.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "https://localhost:8081")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public WalletController(WalletService walletService, UserService userService,UserRepository userRepository) {
        this.walletService = walletService;
        this.userService = userService;
        this.userRepository=userRepository;
    }

    @PostMapping("/add-money")
    public ResponseEntity<Wallet> addMoneyToWallet(@RequestParam("userId") Long userId, @RequestParam("amount") BigDecimal amount) {
        User user = userService.getUserById(userId);
        Wallet wallet = walletService.addMoney(user, amount);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/send-money")
    public ResponseEntity<Wallet> sendMoney(@RequestParam("senderId") Long senderId, @RequestParam("receiverId") Long receiverId, @RequestParam("amount") BigDecimal amount) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(receiverId);
        Wallet wallet = walletService.sendMoney(sender, receiver, amount);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getWalletHistory(@PathVariable Long userId) throws UserException {
        User user = new User(userId);
        List<Transaction> history = walletService.getWalletHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/total-freeze-amount")
    public ResponseEntity<BigDecimal> getTotalFreezeAmount(@RequestParam Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException("User not found with ID: " + userId));
            BigDecimal totalFreezeAmount = walletService.getTotalFrozenAmountForUser(user);
            return ResponseEntity.ok(totalFreezeAmount);
        } catch (UserException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
