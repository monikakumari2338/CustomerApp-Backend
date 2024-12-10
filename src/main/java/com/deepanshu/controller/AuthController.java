package com.deepanshu.controller;

import com.deepanshu.modal.Tier;
import com.deepanshu.modal.Wallet;
import com.deepanshu.repository.WalletRepository;
import com.deepanshu.service.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.config.JwtTokenProvider;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.User;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.request.LoginRequest;
import com.deepanshu.response.AuthResponse;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://localhost:8081")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetails customUserDetails;
    private CartService cartService;

    private WishlistService wishlistService;
    private final JavaMailSender javaMailSender;
    private final RewardService rewardService;

    private final UserService userService;

    private final TierService tierService;
    private final WalletRepository walletRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                          CustomUserDetails customUserDetails, CartService cartService, WishlistService wishlistService,
                          JavaMailSender javaMailSender, RewardService rewardService, UserService userService,
                          TierService tierService, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetails = customUserDetails;
        this.cartService = cartService;
        this.wishlistService = wishlistService;
        this.javaMailSender = javaMailSender;
        this.rewardService = rewardService;
        this.userService = userService;
        this.tierService = tierService;
        this.walletRepository=walletRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user, @RequestParam(required = false) String referralCode) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String mobile = user.getMobile();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {

            throw new UserException("Email Is Already Used With Another Account");
        }
        String otp = generateOTP();

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setMobile(mobile);
        createdUser.setGender(user.getGender());
        createdUser.setAlternativeMobileNumber(user.getAlternativeMobileNumber());
        createdUser.setDateOfBirth(user.getDateOfBirth());
        createdUser.setAnniversaryDate(user.getAnniversaryDate());
        createdUser.setCreatedAt(LocalDateTime.now());
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setOtp(otp);

        if (referralCode == null || referralCode.isEmpty()) {
            referralCode = generateReferralCode();
            createdUser.setReferralCode(referralCode);
            userRepository.save(createdUser);
            rewardService.earnOnSignUp(createdUser);
        } else {
            User referrer = userRepository.findByReferralCode(referralCode);
            if (referrer == null) {
                throw new UserException("Invalid referral code");
            }
            referralCode = generateReferralCode();
            createdUser.setReferralCode(referralCode);
            userRepository.save(createdUser);
            rewardService.earnOnSignUp(createdUser);
            rewardService.earnOnReferral(referrer, createdUser);
        }
        User savedUser = userRepository.save(createdUser);
        sendOTPByEmail(email, otp);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);

        savedUser.setWallet(wallet);
        userRepository.save(savedUser);


        cartService.createCart(savedUser);
        wishlistService.createWishlist(savedUser);

        tierService.calculateMembershipStatus(savedUser, tierService.determineTier(savedUser.getId()));
        userService.saveUser(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token, true);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        System.out.println(username + " ----- " + password);
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setStatus(true);
        authResponse.setJwt(token);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found for email: " + email);
        }
        String otp = generateOTP();
        user.setOtp(otp);
        userRepository.save(user);
        sendOTPByEmail(email, otp);
        return ResponseEntity.ok("OTP sent to your email, please check.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found for email: " + email);
        }
        if (!otp.equals(user.getOtp())) {
            throw new UserException("Invalid OTP");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password reset successful.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOTP(@RequestParam String email, @RequestParam String otp) throws UserException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found for email: " + email);
        }

        if (otp.equals(user.getOtp())) {
            String token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(email, user.getPassword()));
            AuthResponse authResponse = new AuthResponse(token, true);
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            throw new UserException("Invalid OTP");
        }
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("password not match");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String generateOTP() {
        Random random = new Random();
        int otpLength = 6;
        StringBuilder otp = new StringBuilder(otpLength);
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private void sendOTPByEmail(String email, String otp) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Thanks for  Signup in KPMG Customer App '\n' Your OTP for Signup");
            helper.setText("Your OTP for signup is: " + otp);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateReferralCode() {
        String referralCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return referralCode;
    }

    @GetMapping("/referral-link/{userId}")
    public ResponseEntity<String> generateReferralLink(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String referralCode = user.getReferralCode();
        String referralLink = "https://yourdomain.com/signup?referralCode=" + referralCode;
        return ResponseEntity.ok(referralLink);
    }

    // API endpoint to get the username by user ID
    @GetMapping("/userId={userId}")
    public ResponseEntity<String> getUserName(@PathVariable Long userId) {
        try {
            String userName = userService.getUserName(userId);
            return ResponseEntity.ok(userName);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}