package com.deepanshu.controller;

import com.deepanshu.modal.Tier;
import com.deepanshu.service.TierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.UserException;
import com.deepanshu.modal.User;
import com.deepanshu.service.UserService;
import com.deepanshu.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://localhost:8081")
public class UserController {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    private UserService userService;

    private final TierService tierService;


    public UserController(UserService userService, TierService tierService) {
        this.userService = userService;
        this.tierService = tierService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        System.out.println("/api/users/profile");
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserProfileHandler(@PathVariable long id, @RequestBody User userProfile) throws UserException {
        User updateUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException("user not exist"));

        // Update only if the new value is not null
        if (userProfile.getFirstName() != null) {
            updateUser.setFirstName(userProfile.getFirstName());
        }
        if (userProfile.getLastName() != null) {
            updateUser.setLastName(userProfile.getLastName());
        }
        if (userProfile.getAlternativeMobileNumber() != null) {
            updateUser.setAlternativeMobileNumber(userProfile.getAlternativeMobileNumber());
        }
        if (userProfile.getDateOfBirth() != null) {
            updateUser.setDateOfBirth(userProfile.getDateOfBirth());
        }
        if (userProfile.getAnniversaryDate() != null) {
            updateUser.setAnniversaryDate(userProfile.getAnniversaryDate());
        }
        if (userProfile.getGender() != null) {
            updateUser.setGender(userProfile.getGender());
        }
        if (userProfile.getMobile() != null) {
            updateUser.setMobile(userProfile.getMobile());
        }
        userRepository.save(updateUser);
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changeUserPassword(@PathVariable long id, @RequestParam("currentPassword") String currentPassword,
                                                     @RequestParam("newPassword") String newPassword) throws UserException {
        User updateUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));

        if (!userService.verifyCurrentPassword(updateUser.getEmail(), currentPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        updateUser.setPassword(hashedPassword);
        userRepository.save(updateUser);

        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/{userId}/profile-photo")
    public ResponseEntity<String> updateProfilePhoto(
            @PathVariable("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file");
        }
        String fileName = file.getOriginalFilename();
        System.out.println("Uploaded file name: " + fileName);
        return ResponseEntity.ok().body("Profile photo updated successfully: " + fileName);
    }


    @PostMapping("/{userId}/calculate-membership")
    public ResponseEntity<String> calculateMembershipStatus(@PathVariable Long userId) throws UserException {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Tier tier = tierService.determineTier(userId);
        if (tier == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to determine tier");
        }
        tierService.calculateMembershipStatus(user, tier);
        userService.saveUser(user);

        return ResponseEntity.ok("Membership status calculated and updated successfully");
    }

}
