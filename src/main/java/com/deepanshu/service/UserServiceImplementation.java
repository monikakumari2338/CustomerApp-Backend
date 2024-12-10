package com.deepanshu.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.deepanshu.modal.Address;
import com.deepanshu.repository.AddressRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.deepanshu.config.JwtTokenProvider;
import com.deepanshu.exception.UserException;
import com.deepanshu.modal.User;
import com.deepanshu.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    private AddressRepository addressRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImplementation(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AddressRepository addressRepository) {

        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.addressRepository = addressRepository;

    }

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("user not found with this id " + userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        System.out.println("user service");
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        System.out.println("email" + email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("user not exist with this email " + email);
        }
        System.out.println("email user" + user.getEmail());
        return user;
    }

    @Override
    public boolean verifyCurrentPassword(String email, String currentPassword) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }
        // Compare the current password with the password stored in the database
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    @Override
    public boolean changeUserPassword(Long userId, String currentPassword, String newPassword) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException("Current password is incorrect");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return true;
    }

    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Address addAddress(Long userId, Address newAddress) throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        newAddress.setUser(user);
        Address savedAddress = addressRepository.save(newAddress);
        user.getAddresses().add(savedAddress);
        userRepository.save(user);
        return savedAddress;
    }

    @Override
    public User getUserByReferralCode(String referralCode) {
        return userRepository.findByReferralCode(referralCode);
    }

    @Override
    public Address findAddressForUser(Long userId, Address address) {
        List<Address> userAddresses = userRepository.findById(userId)
                .map(User::getAddresses)
                .orElse(Collections.emptyList());

        for (Address userAddress : userAddresses) {
            if (areAddressesEqual(userAddress, address)) {
                return userAddress;
            }
        }

        return null;
    }

    private boolean areAddressesEqual(Address address1, Address address2) {
        return compareStrings(address1.getStreetAddress(), address2.getStreetAddress()) &&
                compareStrings(address1.getCity(), address2.getCity()) &&
                compareStrings(address1.getState(), address2.getState()) &&
                compareStrings(address1.getZipCode(), address2.getZipCode()) &&
                compareStrings(address1.getMobile(), address2.getMobile());
    }

    private boolean compareStrings(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true; // Both are null, so they are considered equal
        }
        if (str1 == null || str2 == null) {
            return false; // One is null, and the other is not, so they are not equal
        }
        return str1.equals(str2); // Both are not null, so compare their values
    }


    //get user name by userId
    @Override
    public String getUserName(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("user Not found"));
        return user.getFirstName();
    }
}
