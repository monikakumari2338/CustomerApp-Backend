package com.deepanshu.service;

import com.deepanshu.exception.UserException;
import com.deepanshu.modal.Address;
import com.deepanshu.modal.User;

public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;

    public boolean verifyCurrentPassword(String email, String currentPassword) throws UserException;

    boolean changeUserPassword(Long userId, String currentPassword, String newPassword) throws UserException;

    boolean existsById(Long userId);

    User saveUser(User user);

    User getUserById(Long userId);

    Address addAddress(Long userId, Address newAddress) throws UserException;

    User getUserByReferralCode(String referralCode);

    Address findAddressForUser(Long userId, Address address);

    //get user name by userId
    String getUserName(Long userId);
}
