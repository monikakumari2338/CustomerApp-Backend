package com.deepanshu.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.deepanshu.modal.*;
import com.deepanshu.repository.*;
import com.deepanshu.request.CancelItemRequest;
import com.deepanshu.request.ExchangeItemRequest;
import com.deepanshu.request.ReturnItemRequest;
import com.deepanshu.user.domain.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import com.deepanshu.Dto.CartDto;
import com.deepanshu.exception.OrderException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderServiceImplementation implements OrderService {

	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private WishlistService wishlistService;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private RewardService rewardService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private StorePickupService storePickupService;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private PromotionService promotionService;

	public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService,
			WishlistService wishlistService, AddressRepository addressRepository, UserRepository userRepository,
			OrderItemService orderItemService, OrderItemRepository orderItemRepository, RewardService rewardService,
			SubscriptionService subscriptionService, StorePickupService storePickupService,
			ProductService productService, TransactionRepository transactionRepository,
			PromotionService promotionService) {
		this.orderRepository = orderRepository;
		this.cartService = cartService;
		this.wishlistService = wishlistService;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.orderItemService = orderItemService;
		this.orderItemRepository = orderItemRepository;
		this.rewardService = rewardService;
		this.subscriptionService = subscriptionService;
		this.storePickupService = storePickupService;
		this.productService = productService;
		this.transactionRepository = transactionRepository;
		this.promotionService = promotionService;
	}

	@Override
	public Order createOrder(User user, Address shippAddress, StorePickup storePickup) {
		Subscription subscription = subscriptionService.getActiveSubscriptionForUser(user);
		shippAddress.setUser(user);
		Address address = addressRepository.save(shippAddress);
		user.getAddresses().add(address);
		userRepository.save(user);

		int redeemedPoints = 0;
		CartDto cart = cartService.findUserCart(user.getId());
//		if (cart != null && !cart.getCartItems().isEmpty()) {
//			redeemedPoints = rewardService.calculateRedeemedPointsagain(user);
//		}
		Wishlist wishlist = wishlistService.findUserWishlist(user.getId());
		List<OrderItem> orderItems = new ArrayList<>();

		// for (CartItem item : cart.getCartItems()) {
		OrderItem orderItem = new OrderItem();

//			orderItem.setPrice(item.getPrice());
//			orderItem.setProduct(item.getProduct());
//			orderItem.setQuantity(item.getQuantity());
//			orderItem.setSize(item.getSize());
//			orderItem.setUserId(item.getUserId());
//			orderItem.setDiscountedPrice(item.getDiscountedPrice());
//
//			// Decrease inventory
		//Product product = item.getProduct();
		// product.decreaseInventory(item.getSize(), item.getQuantity());
		//productService.save(product); // Save the product to update the inventory in the database

		OrderItem createdOrderItem = orderItemRepository.save(orderItem);
		orderItems.add(createdOrderItem);

		Order createdOrder = new Order();
//		createdOrder.setUser(user);
//		createdOrder.setOrderItems(orderItems);
//		createdOrder.setTotalPrice(cart.getTotalPrice());
//		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
//		createdOrder.setDiscounte(cart.getDiscounte());
//		createdOrder.setTotalItem(cart.getTotalItem());
//		createdOrder.setShippingAddress(address);
//		createdOrder.setOrderDate(LocalDateTime.now());
//		createdOrder.setOrderStatus(OrderStatus.PENDING);
//		createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
//		createdOrder.setCreatedAt(LocalDateTime.now());
//		createdOrder.setSubscription(subscription);
//		createdOrder.setStorePickup(storePickup); // store pickup
//		createdOrder.setPromotionDiscount(cart.getPromotion_discount());
//		createdOrder.setPromoCode(new HashMap<>(cart.getPromoCode()));
//
//		if (redeemedPoints > 0) {
//			createdOrder.setRedeemedPoints(redeemedPoints);
//		} else {
//			createdOrder.setRedeemedPoints(0);
//		}
		// Order savedOrder = orderRepository.save(createdOrder);
		for (OrderItem item : orderItems) {
			// item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}
		Map<String, Integer> appliedPromotion = createdOrder.getPromotionList();

		// return savedOrder;
		return null;

	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.PLACED);
		order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
		return order;
	}

	@Transactional
	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.SHIPPED);
		return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.DELIVERED);
		int pointsEarned = (int) (order.getTotalDiscountedPrice() * 0.05); // Redemption points to be 5% of the total
																			// order value Reviewed by Pradeep Jain
		pointsEarned = Math.min(pointsEarned, 100); // Maximum points on an order to be 100 Reviewed by Pradeep Jain
		rewardService.earnReward(order.getUser(), pointsEarned);

		return orderRepository.save(order);
	}

	@Override
	public Order cancledOrder(Long orderId, List<CancelItemRequest> cancelItemRequests,
			CancellationReason cancellationReason, String comments) throws OrderException {
		Order order = findOrderById(orderId);
		List<OrderItem> itemsToCancel = new ArrayList<>();
		int totalRefundPoints = 0;
		int countTotalItemNeedToCancel = cancelItemRequests.size();
		boolean flagForPromotionDeduction = false;
		int countTotalProductOnWhichPromotionApplied = 0;
		// count total on which transactionWise promotion applied
		for (OrderItem orderItem : order.getOrderItems()) {
			// Null checks to prevent NullPointerException
			if (orderItem != null && orderItem.getProduct() != null
					&& orderItem.getProduct().getAppliedPromotion() != null) {
				if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")
						|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")
						|| orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")
						|| orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT")) {
					countTotalProductOnWhichPromotionApplied++;
				}
			}
		}

		System.out.println("\n\n\ncountTotalItemNeedToCancel:" + countTotalItemNeedToCancel
				+ " \norderPromotionAppliedProductsLength:" + countTotalProductOnWhichPromotionApplied);

		if (countTotalProductOnWhichPromotionApplied != countTotalItemNeedToCancel) {
			flagForPromotionDeduction = true;
		}

		for (CancelItemRequest cancelItemRequest : cancelItemRequests) {
			Long orderItemId = cancelItemRequest.getOrderItemId();

			Optional<OrderItem> orderItemOpt = order.getOrderItems().stream()
					.filter(item -> item.getId().equals(orderItemId)).findFirst();

			if (orderItemOpt.isPresent()) {
				OrderItem orderItemToCancel = orderItemOpt.get();
				handleCancelOrderItem(order, orderItemToCancel, orderId, flagForPromotionDeduction);
				itemsToCancel.add(orderItemToCancel);

				double itemDiscountedPrice = orderItemToCancel.getDiscountedPrice() * orderItemToCancel.getQuantity();
				double totalDiscountedPrice = order.getTotalDiscountedPrice();
				int redeemedPoints = order.getRedeemedPoints();
				totalRefundPoints += (int) ((itemDiscountedPrice / totalDiscountedPrice) * redeemedPoints);

				orderItemToCancel.setCancelStatus(CancelStatus.CANCELLED);
				orderItemRepository.save(orderItemToCancel); // Save cancel status to the database
			} else {
				throw new OrderException("Order item not found in the order");
			}
		}

		// Calculate the new totals based on the remaining items in the order
		double newTotalPrice = 0;
		double newTotalDiscountedPrice = 0;
		int newTotalItem = 0;
		boolean allItemsCancelled = true;
		int i = 1;
		for (OrderItem item : order.getOrderItems()) {
			newTotalPrice += item.getPrice();
			newTotalDiscountedPrice += item.getDiscountedPrice();
			newTotalItem += item.getQuantity();

			if (item.getCancelStatus() != CancelStatus.CANCELLED) {
				allItemsCancelled = false;
			}
			System.out.println(i + " item.getPrice():" + item.getPrice() + "\nitem.getQuantity():" + item.getQuantity()
					+ "\nitem.getDiscountedPrice():" + item.getDiscountedPrice());
			i++;
		}
//        order.setTotalPrice(newTotalPrice);
//        order.setTotalDiscountedPrice((int) newTotalDiscountedPrice);
//        order.setTotalItem(newTotalItem);
//        order.setDiscounte((int) (newTotalPrice - newTotalDiscountedPrice));
//        order.setCancellationReason(cancellationReason);

		// Update the redeemed points
		order.setRedeemedPoints(order.getRedeemedPoints() - totalRefundPoints);

		// Set other order details
		if (allItemsCancelled) {
			order.setOrderStatus(OrderStatus.CANCELLED);
		} else {
			order.setOrderStatus(OrderStatus.PARTIAL_CANCELLED);
		}
		order.setComments(comments);

		return orderRepository.save(order);
	}

	private void handleCancelOrderItem(Order order, OrderItem orderItemToCancel, Long orderId,
			boolean flagForPromotionDeduction) throws OrderException {
		User user = order.getUser();
		Product product = orderItemToCancel.getProduct();
		String sizeName = orderItemToCancel.getSize();
		if (sizeName != null && !sizeName.isEmpty()) {
			product.increaseInventory(sizeName, orderItemToCancel.getQuantity());
			productService.save(product);
		} else {
			throw new OrderException("Size information is missing for an order item");
		}
		System.out.println("\n\nOrderId:" + orderId);

		Order orderWantToCancel = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("order not found with given id:" + orderId));

		Map<String, Integer> appliedPromotionList = orderWantToCancel.getPromotionList();

		Wallet userWallet = order.getUser().getWallet();
		BigDecimal refundAmount = BigDecimal.valueOf(orderItemToCancel.getDiscountedPrice());

		// Convert quantity to BigDecimal for division
		BigDecimal quantity = BigDecimal.valueOf(orderItemToCancel.getQuantity());
		BigDecimal payedAmount = BigDecimal.valueOf(orderWantToCancel.getTotalDiscountedPrice());
		BigDecimal cancelProductAmount = BigDecimal.ZERO;
		if (product.getPromotionalDiscountedPrice() != null) {
			BigDecimal promotionalDiscountedPrice = new BigDecimal(product.getPromotionalDiscountedPrice());
			cancelProductAmount = promotionalDiscountedPrice
					.multiply(BigDecimal.valueOf(orderItemToCancel.getQuantity()));
		} else {
			cancelProductAmount = BigDecimal.valueOf(product.getDiscountedPrice() * orderItemToCancel.getQuantity());
		}

		System.out.println("productPrice" + product.getDiscountedPrice() + "\n" + "initialRefundAmmount:" + refundAmount
				+ "\n" + "PayedAmount:" + payedAmount + "\n" + "CancelProductAmount:" + cancelProductAmount + '\n');

		for (Map.Entry<String, Integer> promotion : appliedPromotionList.entrySet()) {
			BigDecimal promotionValue = BigDecimal.valueOf(promotion.getValue());

			// Check if the payed amount minus the canceled product amount is less than the
			// promotion value
			if (payedAmount.subtract(cancelProductAmount).compareTo(promotionValue) < 0) {
				// Calculate the refund amount considering the promotion discount
				refundAmount = cancelProductAmount.subtract(orderWantToCancel.getPromotionDiscount());

				// Optionally log or process the refund amount further
				System.out.println("Refund Amount: " + refundAmount);
			}
		}
		// Store applied promotion on the order
		Map<String, Double> appliedPromotionOnOrder = new HashMap<>();

		if (!order.getPromoCode().isEmpty()) {
			appliedPromotionOnOrder.putAll(order.getPromoCode());
		}

		// remainOrderItem AppliedPromotionSum
		double orderItemAppliedPromotionSum = 0.0;
		// Check if promotion amounts need to be deducted or adjusted
		for (Map.Entry<String, Double> entry : appliedPromotionOnOrder.entrySet()) {
			String promoCode = entry.getKey();
			Double discountAmount = entry.getValue();

			// Assuming a condition to check if a deduction or adjustment is needed
			if (discountAmount > 0) {
				System.out.println("Promo Code: " + promoCode + ", Discount Amount: " + discountAmount);
			} else {
				System.out.println("Promo Code: " + promoCode + " has no discount or needs adjustment.");
			}
			if (promoCode.equals("FIVEHUNDREDOFF") || promoCode.equals("ANNIVERSARY299")
					|| promoCode.equals("FIVEPERCENTOFF") || promoCode.equals("BIRTH10PCT")) {

				Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
						.orElseThrow(() -> new RuntimeException("Promotion not found" + promoCode));

				// check promotionType, is it FLAT or in PERCENTAGE
				String promotionType = null;
				double totalOrderAmount = 0.0;
				double cancelProductTotalAmount = orderItemToCancel.getDiscountedPrice();
				int totalItemWithAppliedPromotion = 0;
				for (OrderItem orderItem : order.getOrderItems()) {
					// Null checks to avoid NullPointerException
					if (orderItem != null && orderItem.getProduct() != null
							&& orderItem.getProduct().getAppliedPromotion() != null) {

						// Print each product's price, quantity, and cancel status
						System.out.println("\nProduct Price: " + orderItem.getDiscountedPrice());
						System.out.println("\nQuantity: " + orderItem.getQuantity());
						System.out.println("\nCancelled Status: " + orderItem.getCancelStatus());

						if ((orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")
								|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")
								|| orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")
								|| orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT"))
								&& (orderItem.getCancelStatus() == null)) {

							if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")
									|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")) {
								promotionType = "FLAT";
							} else {
								promotionType = "PERCENTAGE";
							}

							if (!Objects.equals(orderItemToCancel.getId(), orderItem.getId())) {
								// Check if any of the specified promotion codes are present in the applied
								// promotions
								boolean hasPromotion = orderItem.getProduct().getAppliedPromotion()
										.containsKey("FIVEHUNDREDOFF")
										|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")
										|| orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")
										|| orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT");

								// If there is a promotion, add to the sum
								if (hasPromotion) {
									// Retrieve the promotion amounts and sum them up
									if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("FIVEHUNDREDOFF");
									}
									if (orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("ANNIVERSARY299");
									}
									if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("FIVEPERCENTOFF");
									}
									if (orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("BIRTH10PCT");
									}
								}
							}

							totalOrderAmount += orderItem.getDiscountedPrice();
							totalItemWithAppliedPromotion += orderItem.getQuantity();
						}

					} else {
						System.out.println("OrderItem or related data is null. Skipping...");
					}
				}

				double singleProductPriceBeforePromotion = 0.0;
				if (promotionType != null && promotionType.equals("PERCENTAGE")) {
					double promotionAmount = Double.parseDouble(promotion.getDiscountValue());
					singleProductPriceBeforePromotion = (totalOrderAmount / ((100.00 - promotionAmount) / 100))
							/ totalItemWithAppliedPromotion;
				} else {
					System.out.println("\n\nsingleProductAmountFromElsePart\n");
					singleProductPriceBeforePromotion = (totalOrderAmount
							+ Double.parseDouble(promotion.getDiscountValue())) / totalItemWithAppliedPromotion;
				}

				double singleProductPriceAfterPromotion = totalOrderAmount / totalItemWithAppliedPromotion;
				int remainItemCount = totalItemWithAppliedPromotion - orderItemToCancel.getQuantity();
				double appliedPromotionDiscount = singleProductPriceBeforePromotion - singleProductPriceAfterPromotion;
				double deductionCalculation = (totalOrderAmount - cancelProductTotalAmount)
						+ (remainItemCount * appliedPromotionDiscount);

				if (flagForPromotionDeduction && remainItemCount > 0) {
					if (deductionCalculation < promotion.getMinOrderValue()
							&& deductionCalculation != totalOrderAmount) {
						refundAmount = refundAmount.subtract(BigDecimal.valueOf(orderItemAppliedPromotionSum));
						System.out.println("Refund amount to user is:" + refundAmount);
					}
				}
				System.out.println("\n\nTotal order amount after discounts: " + totalOrderAmount + ", "
						+ "\nTotal amount of the canceled product after discount: " + cancelProductTotalAmount + ", "
						+ "\nSingle product price before applying promotion: " + singleProductPriceBeforePromotion
						+ ", " + "\nSingle product price after applying promotion: " + singleProductPriceAfterPromotion
						+ ", " + "\nRemaining item count after cancellation: " + remainItemCount + ", "
						+ "\nApplied promotion discount per remaining item: " + appliedPromotionDiscount + ", "
						+ "\nDeduction calculation (to check minimum order value for refund adjustment): "
						+ deductionCalculation + "\ntotalItemWithAppliedPromotion:" + totalItemWithAppliedPromotion
						+ "\ncancelProductTotalAmount:" + cancelProductTotalAmount + "\norderItemAppliedPromotionSum:"
						+ orderItemAppliedPromotionSum);
			}
		}

		// Use divide method for BigDecimal division
		BigDecimal refundPerItem = refundAmount;
		userWallet.setBalance(userWallet.getBalance().add(refundPerItem));
		walletRepository.save(userWallet);

		// Create a transaction entry to reflect the frozen amount
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setTransactionType(TransactionType.REFUND_AGAINST_ORDER_CANCELLATION);
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setAmount(refundPerItem);
		transactionRepository.save(transaction);

		int redeemedPoints = order.getRedeemedPoints();
		double totalDiscountedPrice = order.getTotalDiscountedPrice();

		double itemDiscountedPrice = orderItemToCancel.getDiscountedPrice() * orderItemToCancel.getQuantity();
		int refundedPoints = (int) ((itemDiscountedPrice / totalDiscountedPrice) * redeemedPoints);
		rewardService.revertPoints(order.getUser(), refundedPoints);

		orderItemToCancel.setCancelStatus(CancelStatus.CANCELLED);
		orderItemRepository.save(orderItemToCancel);
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> opt = orderRepository.findById(orderId);
		if (opt.isPresent()) {
			Order order = opt.get();
			return order;
		}
		throw new OrderException("order not exist with id " + orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders = orderRepository.getUsersOrders(userId);
		// Reverse the order of the list
		Collections.reverse(orders);
		return orders;
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		orderRepository.deleteById(orderId);
	}

	@Override
	public Order returnProduct(Long orderId, List<ReturnItemRequest> returnItemRequests, ReturnReason returnReason,
			String comments, MultipartFile file) throws OrderException {
		Order order = findOrderById(orderId);
		List<OrderItem> itemsToReturn = new ArrayList<>();
		int totalRefundPoints = 0;
		int countTotalItemNeedToCancel = returnItemRequests.size();
		boolean flagForPromotionDeduction = false;
		int countTotalPromotionAppliedProductCount = 0;
		// count total on which transactionWise promotion applied
		for (OrderItem orderItem : order.getOrderItems()) {
			// Null checks to prevent NullPointerException
			if (orderItem != null && orderItem.getProduct() != null
					&& orderItem.getProduct().getAppliedPromotion() != null) {
				if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")
						|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")
						|| orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")
						|| orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT")) {
					countTotalPromotionAppliedProductCount++;
				}
			}
		}
		System.out.println("\n\n\ncountTotalItemNeedToCancel:" + countTotalItemNeedToCancel
				+ " \norderPromotionAppliedProductsLength:" + countTotalPromotionAppliedProductCount);

		if (countTotalPromotionAppliedProductCount != countTotalItemNeedToCancel) {
			flagForPromotionDeduction = true;
		}

		for (ReturnItemRequest returnItemRequest : returnItemRequests) {
			Long orderItemId = returnItemRequest.getOrderItemId();

			Optional<OrderItem> orderItemOpt = order.getOrderItems().stream()
					.filter(item -> item.getId().equals(orderItemId)).findFirst();

			if (orderItemOpt.isPresent()) {
				OrderItem orderItemToReturn = orderItemOpt.get();
				handleReturnOrderItem(order, orderItemToReturn, flagForPromotionDeduction);
				itemsToReturn.add(orderItemToReturn);

				double itemDiscountedPrice = orderItemToReturn.getDiscountedPrice() * orderItemToReturn.getQuantity();
				double totalDiscountedPrice = order.getTotalDiscountedPrice();
				int redeemedPoints = order.getRedeemedPoints();
				totalRefundPoints += (int) ((itemDiscountedPrice / totalDiscountedPrice) * redeemedPoints);

				orderItemToReturn.setReturnStatus(ReturnStatus.RETURNED);
				orderItemRepository.save(orderItemToReturn); // Save return status to the database
			} else {
				throw new OrderException("Order item not found in the order");
			}
		}

		// Calculate the new totals based on the remaining items in the order
		double newTotalPrice = 0;
		double newTotalDiscountedPrice = 0;
		int newTotalItem = 0;
		boolean allItemsReturned = true;
		for (OrderItem item : order.getOrderItems()) {
			newTotalPrice += item.getPrice() * item.getQuantity();
			newTotalDiscountedPrice += item.getDiscountedPrice() * item.getQuantity();
			newTotalItem += item.getQuantity();

			if (item.getReturnStatus() != ReturnStatus.RETURNED) {
				allItemsReturned = false;
			}
		}
//        order.setTotalPrice(newTotalPrice);
//        order.setTotalDiscountedPrice((int) newTotalDiscountedPrice);
//        order.setTotalItem(newTotalItem);
//        order.setDiscounte((int) (newTotalPrice - newTotalDiscountedPrice));

		// Update the redeemed points
		order.setRedeemedPoints(order.getRedeemedPoints() - totalRefundPoints);

		// Set other order details
		if (file != null && !file.isEmpty()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			order.setAttachment(fileName);
		}

		if (allItemsReturned) {
			order.setOrderStatus(OrderStatus.RETURNED);
		} else {
			order.setOrderStatus(OrderStatus.PARTIAL_RETURNED);
		}
		order.setReturnReason(returnReason);
		order.setComments(comments);

		return orderRepository.save(order);
	}

	private void handleReturnOrderItem(Order order, OrderItem orderItemToReturn, Boolean flagForPromotionDeduction)
			throws OrderException {
		User user = order.getUser();
		Product product = orderItemToReturn.getProduct();
		String sizeName = orderItemToReturn.getSize();
		if (sizeName != null && !sizeName.isEmpty()) {
			product.increaseInventory(sizeName, orderItemToReturn.getQuantity());
			productService.save(product);
		} else {
			throw new OrderException("Size information is missing for an order item");
		}

		Wallet userWallet = order.getUser().getWallet();
		BigDecimal refundAmount = BigDecimal.valueOf(orderItemToReturn.getDiscountedPrice());

		// Store applied promotion on the order
		Map<String, Double> appliedPromotionOnOrder = new HashMap<>();

		if (!order.getPromoCode().isEmpty()) {
			appliedPromotionOnOrder.putAll(order.getPromoCode());
		}

		// remainOrderItem AppliedPromotionSum
		double orderItemAppliedPromotionSum = 0.0;
		// Check if promotion amounts need to be deducted or adjusted
		for (Map.Entry<String, Double> entry : appliedPromotionOnOrder.entrySet()) {
			String promoCode = entry.getKey();
			Double discountAmount = entry.getValue();

			// Assuming a condition to check if a deduction or adjustment is needed
			if (discountAmount > 0) {
				System.out.println("Promo Code: " + promoCode + ", Discount Amount: " + discountAmount);
			} else {
				System.out.println("Promo Code: " + promoCode + " has no discount or needs adjustment.");
			}
			if (promoCode.equals("FIVEHUNDREDOFF") || promoCode.equals("ANNIVERSARY299")
					|| promoCode.equals("FIVEPERCENTOFF") || promoCode.equals("BIRTH10PCT")) {

				Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
						.orElseThrow(() -> new RuntimeException("Promotion not found" + promoCode));

				// check promotionType, is it FLAT or in PERCENTAGE
				String promotionType = null;
				double totalOrderAmount = 0.0;
				double cancelProductTotalAmount = orderItemToReturn.getDiscountedPrice();
				int totalItemWithAppliedPromotion = 0;
				for (OrderItem orderItem : order.getOrderItems()) {
					// Null checks to avoid NullPointerException
					if (orderItem != null && orderItem.getProduct() != null
							&& orderItem.getProduct().getAppliedPromotion() != null) {

						// Print each product's price, quantity, and cancel status
						System.out.println("\nProduct Price: " + orderItem.getDiscountedPrice());
						System.out.println("\nQuantity: " + orderItem.getQuantity());
						System.out.println("\nCancelled Status: " + orderItem.getCancelStatus());

						if ((orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")
								|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")
								|| orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")
								|| orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT"))
								&& (orderItem.getCancelStatus() == null)) {

							if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")
									|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")) {
								promotionType = "FLAT";
							} else {
								promotionType = "PERCENTAGE";
							}

							if (!Objects.equals(orderItemToReturn.getId(), orderItem.getId())) {
								// Check if any of the specified promotion codes are present in the applied
								// promotions
								boolean hasPromotion = orderItem.getProduct().getAppliedPromotion()
										.containsKey("FIVEHUNDREDOFF")
										|| orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")
										|| orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")
										|| orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT");

								// If there is a promotion, add to the sum
								if (hasPromotion) {
									// Retrieve the promotion amounts and sum them up
									if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEHUNDREDOFF")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("FIVEHUNDREDOFF");
									}
									if (orderItem.getProduct().getAppliedPromotion().containsKey("ANNIVERSARY299")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("ANNIVERSARY299");
									}
									if (orderItem.getProduct().getAppliedPromotion().containsKey("FIVEPERCENTOFF")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("FIVEPERCENTOFF");
									}
									if (orderItem.getProduct().getAppliedPromotion().containsKey("BIRTH10PCT")) {
										orderItemAppliedPromotionSum += orderItem.getProduct().getAppliedPromotion()
												.get("BIRTH10PCT");
									}
								}
							}
							totalOrderAmount += orderItem.getDiscountedPrice();
							totalItemWithAppliedPromotion += orderItem.getQuantity();
						}

					} else {
						System.out.println("OrderItem or related data is null. Skipping...");
					}
				}

				double singleProductPriceBeforePromotion = 0.0;
				if (promotionType != null && promotionType.equals("PERCENTAGE")) {
					double promotionAmount = Double.parseDouble(promotion.getDiscountValue());
					singleProductPriceBeforePromotion = (totalOrderAmount / ((100.00 - promotionAmount) / 100))
							/ totalItemWithAppliedPromotion;
				} else {
					singleProductPriceBeforePromotion = (totalOrderAmount
							+ Double.parseDouble(promotion.getDiscountValue())) / totalItemWithAppliedPromotion;
				}

				double singleProductPriceAfterPromotion = totalOrderAmount / totalItemWithAppliedPromotion;
				int remainItemCount = totalItemWithAppliedPromotion - orderItemToReturn.getQuantity();
				double appliedPromotionDiscount = singleProductPriceBeforePromotion - singleProductPriceAfterPromotion;
				double deductionCalculation = (totalOrderAmount - cancelProductTotalAmount)
						+ (remainItemCount * appliedPromotionDiscount);

				if (flagForPromotionDeduction && remainItemCount > 0) {
					if (deductionCalculation < promotion.getMinOrderValue()
							&& deductionCalculation != totalOrderAmount) {
						refundAmount = refundAmount.subtract(BigDecimal.valueOf(orderItemAppliedPromotionSum));
					}
				}
				System.out.println("\n\nTotal order amount after discounts: " + totalOrderAmount + ", "
						+ "\nTotal amount of the canceled product after discount: " + cancelProductTotalAmount + ", "
						+ "\nSingle product price before applying promotion: " + singleProductPriceBeforePromotion
						+ ", " + "\nSingle product price after applying promotion: " + singleProductPriceAfterPromotion
						+ ", " + "\nRemaining item count after cancellation: " + remainItemCount + ", "
						+ "\nApplied promotion discount per remaining item: " + appliedPromotionDiscount + ", "
						+ "\nDeduction calculation (to check minimum order value for refund adjustment): "
						+ deductionCalculation + "\ntotalAppliedPromotionQuantity:" + totalItemWithAppliedPromotion
						+ "\ncancelProductQunatity:" + cancelProductTotalAmount);
			}
		}

		userWallet.setBalance(userWallet.getBalance().add(refundAmount));
		walletRepository.save(userWallet);

		// Create a transaction entry to reflect the frozen amount
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setTransactionType(TransactionType.REFUND_AGAINST_ORDER_RETURNED);
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setAmount(refundAmount);
		transactionRepository.save(transaction);

		int redeemedPoints = order.getRedeemedPoints();
		double totalDiscountedPrice = order.getTotalDiscountedPrice();
		double itemDiscountedPrice = orderItemToReturn.getDiscountedPrice() * orderItemToReturn.getQuantity();
		int refundedPoints = (int) ((itemDiscountedPrice / totalDiscountedPrice) * redeemedPoints);
		rewardService.revertPoints(order.getUser(), refundedPoints);

		orderItemToReturn.setReturnStatus(ReturnStatus.RETURNED);
		orderItemRepository.save(orderItemToReturn);
	}

	@Override
	@Transactional
	public Order exchangeProduct(Long orderId, List<ExchangeItemRequest> exchangeItemRequests,
			ReturnReason returnReason, String comments) throws OrderException {
		Order originalOrder = findOrderById(orderId);

		// Create a new exchange order
		Order exchangeOrder = new Order();
		exchangeOrder.setUser(originalOrder.getUser());
		exchangeOrder.setOrderStatus(OrderStatus.EXCHANGED);
		exchangeOrder.setOrderDate(LocalDateTime.now());
		exchangeOrder.setCreatedAt(LocalDateTime.now());
		exchangeOrder.setReturnReason(returnReason);
		exchangeOrder.setComments(comments);
		exchangeOrder.setShippingAddress(originalOrder.getShippingAddress());

		List<OrderItem> exchangedOrderItems = new ArrayList<>();

		for (ExchangeItemRequest exchangeRequest : exchangeItemRequests) {
			Long orderItemId = exchangeRequest.getOrderItemId();
			Long newProductId = exchangeRequest.getNewProductId();
			int quantity = exchangeRequest.getQuantity();
			String newSize = exchangeRequest.getSize();

			// Find the original order item to be exchanged
			OrderItem originalOrderItem = originalOrder.getOrderItems().stream()
					.filter(item -> item.getId().equals(orderItemId)).findFirst()
					.orElseThrow(() -> new OrderException("Order item not found in the order"));

			// Remove the original order item from the original order
			originalOrderItem.setExchangeStatus(ExchangeStatus.COMPLETED); // Update exchange status

			// Create a new order item for the exchange order
			OrderItem exchangedOrderItem = new OrderItem();
			exchangedOrderItem.setProduct(productService.getProductById(newProductId)); // Assuming
																						// productService.getProductById
																						// exists and retrieves the
																						// product
			exchangedOrderItem.setQuantity(quantity);
			exchangedOrderItem.setPrice(originalOrderItem.getPrice()); // Copy price from the original order item
			exchangedOrderItem.setDiscountedPrice(originalOrderItem.getDiscountedPrice()); // Copy discounted price from
																							// the original order item
			exchangedOrderItem.setSize(newSize);
			exchangedOrderItem.setExchangeStatus(ExchangeStatus.REQUESTED); // Set exchange status

			// Handle inventory update for the original order item size
			Product originalProduct = originalOrderItem.getProduct();
			ProductDetails originalSize = originalProduct.getDetails().stream()
					.filter(size -> size.getName().equals(originalOrderItem.getSize())).findFirst()
					.orElseThrow(() -> new OrderException("Size not found in the original product"));

			originalSize.increaseQuantity(originalOrderItem.getQuantity()); // Restore original size quantity
			productService.save(originalProduct); // Save changes to the original product

			// Handle inventory update for the exchanged order item size
			Product exchangedProduct = exchangedOrderItem.getProduct();
			ProductDetails exchangedSize = exchangedProduct.getDetails().stream()
					.filter(size -> size.getName().equals(newSize)).findFirst()
					.orElseThrow(() -> new OrderException("Size not found in the exchanged product"));

			if (exchangedSize.getQuantity() < exchangedOrderItem.getQuantity()) {
				throw new OrderException("Insufficient stock for exchanged size " + exchangedSize.getName());
			}

			exchangedSize.decreaseQuantity(exchangedOrderItem.getQuantity()); // Decrease exchanged size quantity
			productService.save(exchangedProduct); // Save changes to the exchanged product

			// Set the exchanged order item in the exchange order
			exchangedOrderItem.setOrder(exchangeOrder);
			exchangedOrderItems.add(exchangedOrderItem);
		}

		// Set exchanged order items in the exchange order
		exchangeOrder.setOrderItems(exchangedOrderItems);
		exchangeOrder.setReturnReason(returnReason);
		exchangeOrder.setComments(comments);

		// Save the exchange order
		Order savedExchangeOrder = orderRepository.save(exchangeOrder);

		// Update the original order in the database
		orderRepository.save(originalOrder);

		return savedExchangeOrder;
	}

	@Override
	public Order createOrderForSubscriptionDelivery(Subscription subscription) {
		Order order = new Order();

		// Set relevant details for the order
		order.setUser(subscription.getUser());
		order.setSubscription(subscription);
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDateTime.now());

		// Assuming the product is associated with the subscription
		Product product = subscription.getProduct();

		// Create order items for each delivery day
		List<OrderItem> orderItems = new ArrayList<>();
		for (LocalDate deliveryDay : subscription.getDeliveryDays()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(1); // Assuming one quantity for simplicity
			orderItem.setPrice(product.getPrice()); // Convert BigDecimal to double
			orderItem.setDiscountedPrice(product.getDiscountedPrice()); // Convert BigDecimal to double
			orderItem.setDeliveryDate(deliveryDay.atStartOfDay());
			orderItems.add(orderItem);
		}

		// Set the order items in the order
		order.setOrderItems(orderItems);

		// Calculate and set total price and total discounted price
		double totalPrice = orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
		double totalDiscountedPrice = orderItems.stream().mapToDouble(OrderItem::getDiscountedPrice).sum();

		order.setTotalPrice(totalPrice);
		order.setTotalDiscountedPrice((int) totalDiscountedPrice);

		// Save the order in the database
		Order savedOrder = orderRepository.save(order);

		// You can perform additional operations like sending confirmation emails,
		// updating inventory, etc.

		return savedOrder;
	}

	// filter the order by orderItem ids
	@Override
	public List<OrderItem> filteredOrderBasedOnOrderItems(Long orderId, List<Long> cancelledOrderItemsId) {
		// Check if orderId exists in DB or not
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not exist with orderId: " + orderId));

		// Store orderItems from the order
		List<OrderItem> orderItems = order.getOrderItems();

		// Filter order items based on the cancelledOrderItemsId
		List<OrderItem> canceledOrderItems = orderItems.stream()
				.filter(orderItem -> cancelledOrderItemsId != null && cancelledOrderItemsId.contains(orderItem.getId()))
				.toList();

		order.setFilteredSum(
				String.valueOf(canceledOrderItems.stream().mapToDouble(OrderItem::getDiscountedPrice).sum()));
		orderRepository.save(order);
		return canceledOrderItems;
	}

	// calculate refund amount based on cancel products in the order
//    double refundAmountBasedOnCancelItems(Long orderId,List<Long>cancelledOrderItemsId){
//
//    }

}
