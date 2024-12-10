package com.deepanshu.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.deepanshu.modal.*;
import com.deepanshu.repository.OrderRepository;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.OrderException;
import com.deepanshu.exception.UserException;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "https://localhost:8081")
public class OrderController {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private TierService tierService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WalletService walletService;

    @Autowired
    private StorePickupService storePickupService;

    public OrderController(OrderService orderService, UserService userService, TierService tierService,StorePickupService storePickupService) {
        this.orderService = orderService;
        this.userService = userService;
        this.tierService = tierService;
        this.storePickupService=storePickupService;
    }

    @PostMapping("/")
    public ResponseEntity<Order> createOrderHandler(@RequestBody Address spippingAddress,
                                                    @RequestParam(required = false) Long storePickupId,
                                                    @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        User user = userService.findUserProfileByJwt(jwt);
        Address existingAddress = userService.findAddressForUser(user.getId(), spippingAddress);
        if (existingAddress == null) {
            existingAddress = userService.addAddress(user.getId(), spippingAddress);
        }
        Order order;
        if (storePickupId != null) {
            StorePickup storePickup = storePickupService.getStorePickupById(storePickupId)
                    .orElseThrow(() -> new OrderException("Store pickup location not found."));
            order = orderService.createOrder(user, existingAddress, storePickup);
        } else {
            order = orderService.createOrder(user, existingAddress,null);
        }
        BigDecimal orderTotalAmount = BigDecimal.valueOf(order.getTotalDiscountedPrice());
        Wallet updatedWallet = walletService.payForOrderWithWallet(user, orderTotalAmount);
        Order confirmedOrder = orderService.confirmedOrder(order.getId());
        return new ResponseEntity<>(confirmedOrder, HttpStatus.OK);
    }



    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistoryHandler(@RequestHeader("Authorization")
                                                                String jwt) throws OrderException, UserException {

        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization")
    String jwt) throws OrderException, UserException {

        User user = userService.findUserProfileByJwt(jwt);
        Order orders = orderService.findOrderById(orderId);
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<User> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody Address updatedAddress) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Find the address to update
            Optional<Address> optionalAddress = user.getAddresses().stream()
                    .filter(address -> address.getId().equals(addressId))
                    .findFirst();

            if (optionalAddress.isPresent()) {
                Address addressToUpdate = optionalAddress.get();
                addressToUpdate.setFirstName(updatedAddress.getFirstName());
                addressToUpdate.setLastName(updatedAddress.getLastName());
                addressToUpdate.setStreetAddress(updatedAddress.getStreetAddress());
                addressToUpdate.setCity(updatedAddress.getCity());
                addressToUpdate.setState(updatedAddress.getState());
                addressToUpdate.setZipCode(updatedAddress.getZipCode());
                addressToUpdate.setMobile(updatedAddress.getMobile());

                userRepository.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<Address>> searchAddress(
            @PathVariable Long userId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);

        if (user.getId().equals(userId)) {
            List<Address> matchingAddresses = user.getAddresses().stream()
                    .filter(address -> addressMatchesCriteria(address, keyword, city, state, firstName, lastName))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(matchingAddresses, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean addressMatchesCriteria(Address address, String keyword, String city, String state, String firstName, String lastName) {
        boolean matchesKeyword = keyword == null || addressContainsKeyword(address, keyword);
        boolean matchesCity = city == null || address.getCity().contains(city);
        boolean matchesState = state == null || address.getState().contains(state);
        boolean matchesFirstname = firstName == null || address.getFirstName().contains(firstName);
        boolean matchesLastname = lastName == null || address.getLastName().contains(lastName);

        return matchesKeyword && matchesCity && matchesState && matchesFirstname && matchesLastname;
    }

    private boolean addressContainsKeyword(Address address, String keyword) {
        return address.getFirstName().contains(keyword)
                || address.getLastName().contains(keyword)
                || address.getStreetAddress().contains(keyword)
                || address.getCity().contains(keyword)
                || address.getState().contains(keyword)
                || address.getZipCode().contains(keyword)
                || address.getMobile().contains(keyword);
    }

    @PostMapping("/addresses")
    public ResponseEntity<Address> addAddressHandler(@RequestBody Address newAddress,
                                                     @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Address addedAddress = userService.addAddress(user.getId(), newAddress);
        return new ResponseEntity<>(addedAddress, HttpStatus.CREATED);
    }

}
