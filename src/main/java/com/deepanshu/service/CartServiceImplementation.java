package com.deepanshu.service;

import com.deepanshu.exception.UserException;
import com.deepanshu.modal.*;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.PromotionRepository;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.user.domain.DiscountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepanshu.Dto.CartDto;
import com.deepanshu.Dto.CartDto.AppliedPromosInfoDto;
import com.deepanshu.Dto.CartItemsDto;
import com.deepanshu.Dto.CartItemsDto.AttributesInfoDto;
import com.deepanshu.exception.CartItemException;
import com.deepanshu.exception.ProductException;
import com.deepanshu.repository.CartRepository;
import com.deepanshu.request.AddItemRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Float.parseFloat;

@Service
public class CartServiceImplementation implements CartService {

	private CartRepository cartRepository;
	private CartItemService cartItemService;
	private UserService userService;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	@Autowired
	private PromotionRepository promotionRepository;

	@Autowired
	private PromotionServiceImplementation promotionServiceImplementation;

	public int i = 1;

	public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService,
			ProductService productService, UserService userService, PromotionService promotionService,
			UserRepository userRepository, ProductRepository productRepository, BestDealService bestDealService,
			PromotionRepository promotionRepository) {
		this.cartRepository = cartRepository;
		this.cartItemService = cartItemService;
		this.userService = userService;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.promotionRepository = promotionRepository;
	}

	@Override
	public Cart createCart(User user) {

		Cart cart = new Cart();
		cart.setUser(user);
		Cart createdCart = cartRepository.save(cart);
		return createdCart;
	}

	@Override
	public void setExpressDelivery(Long userId, boolean expressDelivery) { // express delivery
		Cart cart = cartRepository.findByUserId(userId);
		if (cart != null) {
			cart.setExpressDelivery(expressDelivery);
			cartRepository.save(cart);
		} else {
			throw new IllegalArgumentException("Cart not found for user ID: " + userId);
		}
	}

	public CartDto findUserCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		int totalQuantity = 0;

		Set<CartItemsDto> cartItemsDto = new HashSet<>();

		for (CartItem cartsItem : cart.getCartItems()) {
			totalPrice += cartsItem.getProduct().getPrice() * cartsItem.getQuantity();
			totalQuantity += cartsItem.getQuantity();
			totalDiscountedPrice += cartsItem.getDiscountedPrice();
			Product product = productRepository.findProductBySku(cartsItem.getSku());
			ProductDetails details = product.getDetails().iterator().next();
			// System.out.println("product "+product.getDetails().iterator().next());
			AttributesInfoDto attributesInfoDto = new AttributesInfoDto(details.getSku(), details.getSize(),
					details.getColor());
			cartItemsDto.add(new CartItemsDto(product.getId(), cartsItem.getId(), cartsItem.getProduct().getTitle(),
					cartsItem.getQuantity(), cartsItem.getPrice(), details.getImageData(), attributesInfoDto));
		}

		cart.setTotalPrice(totalPrice);
		cart.setTotalItem(cart.getCartItems().size());
		cart.setTotalDiscountedPrice(totalDiscountedPrice + (cart.isExpressDelivery() ? 100 : 0));
		cart.setDiscounte(totalPrice - totalDiscountedPrice);
		cart.setTotalItem(totalItem);

		cart = cartRepository.save(cart);

		Promotion promotion = promotionRepository
				.findByPromotionCode(cart.getPromoCode().entrySet().iterator().next().getKey()).get();

		AppliedPromosInfoDto appliedPromosInfoDto = new AppliedPromosInfoDto(
				cart.getPromoCode().entrySet().iterator().next().getKey(),
				cart.getPromoCode().entrySet().iterator().next().getValue(), promotion.getPromotionType());

		CartDto cartDto = new CartDto(cart.getId(), userId, cartItemsDto, appliedPromosInfoDto,
				cart.getCartItems().size(), totalQuantity, totalPrice, totalDiscountedPrice, cart.getDeliveryCharge());

		System.out.println("cartDto :" + cartDto);
		return cartDto;

	}

	@Override
	public String addCartItem(Long userId, AddItemRequest req)
			throws ProductException, UserException, CartItemException {
		Cart cart = cartRepository.findByUserId(userId);

		Product productDetails = productRepository.findProductBySku(req.getSku());
//		System.out.println("productDetails :" + productDetails);
//		System.out.println("productDetails get id  :" + productDetails.getBrand());
		Product product = productRepository.findById(productDetails.getId()).get();
		System.out.println("product :" + product.getPrice());
		if (cart.getPromotion_discount() == null) {
			cart.setPromotion_discount(BigDecimal.ZERO);
		}

		CartItem isCartPresent = cartItemService.isCartItemExist(cart, req.getSku(), userId);
		System.out.println("isPresent :" + isCartPresent);
		if (isCartPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setCategory(product.getCategory().getName());
			cartItem.setPrice(product.getPrice());
			cartItem.setDiscountedPrice(product.getDiscountedPrice());
			cartItem.setUserId(userId);
			cartItem.setColor(productDetails.getColor());
			cartItem.setProduct(product);
			cartItem.setSku(req.getSku());

			CartItem createdCartItem = cartItemService.createCartItem(cartItem);
			cart.getCartItems().add(createdCartItem);
			cartRepository.save(cart);

			// Apply BOGO promotion if the product is eligible for BOGO
			if (product.isEligibleForBogo()) {
				try {
					applyBogoPromotion(userId);
				} catch (ProductException e) {
					throw new ProductException("Error applying BOGO promotion: " + e.getMessage());
				}
			} else {
				// Apply the best item-wise and transaction-wise promotions only if the product
				// is not eligible for BOGO
				try {
					applyBestTransactionWisePromotion(userId);
					applyBestItemWisePromotion(userId);
				} catch (ProductException e) {
					throw new ProductException("Error applying promotions: " + e.getMessage());
				}
			}
			cartRepository.save(cart);
		} else {
			CartItem item = cartItemService.updateCartItem(userId, isCartPresent.getId(), req);
			cart.getCartItems().add(item);
			cartRepository.save(cart);

		}
		// Set the promotion code only if a promotion was applied.
//        applyFivePercentOFFTransactionWise(userId);
//        applyFiveHundredOFFTransactionWise(userId);
		applyBestPromotionOnCart(userId);
////        applyItemWiseTwoHundredOFFOnProduct(userId,false);
//        applyItemWiseTwentyPercentOFFOnProduct(userId,false);
//        anniversaryPromotionOnCart(userId,false);
		return "Item Add To Cart";
	}

	public void clearCart(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);

		if (cart != null) {
			User user = cart.getUser();
			if (user != null) {
				user.setDiscountApplied(false);
				userService.saveUser(user);
			}

//            //remove all promoCodes applied on the cart
//            Map<String,Double>promoCodeList=new HashMap<>();
//            cart.setPromoCode(promoCodeList);
//
//            //remove all promoCodes applied on the cartProducts
//            for(CartItem cartItem:cart.getCartItems()){
//                cartItem.getProduct().setAppliedPromotion(promoCodeList);
//            }

			cart.getCartItems().clear();
			cart.setExpressDelivery(false);
			cart.setPromoCode(null);
			cart.setPromotion_discount(BigDecimal.valueOf(0.0));
			cartRepository.save(cart);
		}
	}

	@Override
	public void applyDiscountToUserCart(Long userId, int discountPercentage) {
		User user = userService.getUserById(userId);
		if (user != null && !user.isDiscountApplied()) {
			Cart cart = cartRepository.findByUserId(userId);
			if (cart != null) {
				int totalCartValue = calculateTotalCartValue(cart);
				Tier tier = determineTier(totalCartValue);
				if (tier != null) {
					int threshold = determineThreshold(tier);
					if (totalCartValue >= threshold) {
						applyDiscounts();
						user.setDiscountApplied(true);
						userService.saveUser(user);
					}
				}
			}
		}
	}

	private Tier determineTier(int totalCartValue) {
		if (totalCartValue >= 5000) {
			return Tier.PLATINUM;
		} else if (totalCartValue >= 3000) {
			return Tier.GOLD;
		} else if (totalCartValue >= 1000) {
			return Tier.SILVER;
		} else {
			return null;
		}
	}

	private int determineThreshold(Tier tier) {
		switch (tier) {
		case SILVER:
			return 1000;
		case GOLD:
			return 3000;
		case PLATINUM:
			return 5000;
		default:
			return 0;
		}
	}

	private int calculateTotalCartValue(Cart cart) {
		int totalCartValue = 0;
		for (CartItem cartItem : cart.getCartItems()) {
			totalCartValue += cartItem.getDiscountedPrice();
		}
		return totalCartValue;
	}

	private void applyDiscounts() {
//		for (CartItem cartItem : cart.getCartItems()) {
//			int discountedPrice = (int) (cartItem.getDiscountedPrice() * (1 - (discountPercentage / 100.0)));
//			cartItem.setDiscountedPrice(discountedPrice);
//		}
//		cartRepository.save(cart);
	}

	@Override
	public BigDecimal calculateDiscount(Long userId, String promoCode) {

		return null;
	}

	@Override
	public void removeDiscount(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			throw new IllegalArgumentException("Cart not found for user ID: " + userId);
		}

		BigDecimal totalCalculatedDiscount = calculateDiscount(userId, "");
		int totalDiscountedPrice = cart.getTotalDiscountedPrice();
		int newTotalDiscountedPrice = totalDiscountedPrice + totalCalculatedDiscount.intValue();

		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			int quantity = cartItem.getQuantity();
			int discountedPrice = product.getDiscountedPrice() * quantity;
			cartItem.setDiscountedPrice(discountedPrice);
		}

		// Reset total price and total discounted price
		int totalPrice = 0;
		for (CartItem cartItem : cart.getCartItems()) {
			cartItem.getSku();
			totalPrice += cartItem.getPrice();
		}
		cart.setTotalPrice(totalPrice);
		cart.setTotalDiscountedPrice(newTotalDiscountedPrice);
		cart.setPromoCode(null);
		cart.setPromotion_discount(null);

		// Save updated cart
		cartRepository.save(cart);
	}

	@Override
	public BigDecimal getDiscountAmount(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			throw new IllegalArgumentException("Cart not found for user ID: " + userId);
		}

		// Assuming the promo code is valid and we calculate the discount
		return calculateDiscount(userId, "");
	}

	@Override
	public void removeExpressDelivery(Long userId) {
		Cart cart = cartRepository.findByUserId(userId);
		if (cart != null) {
			cart.setExpressDelivery(false);
			cartRepository.save(cart);
		} else {
			throw new IllegalArgumentException("Cart not found for userId" + userId);
		}
	}

	@Override
	public BigDecimal applyBirthdayDiscount(Long userId) throws UserException {
		return null;
	}

	@Override
	public BigDecimal applyAnniversaryDiscount(Long userId) throws UserException {

		return null;
	}

	@Override
	public void applyBogoPromotion(Long userId) throws ProductException {

	}

	public void applyBestItemWisePromotion(Long userId) throws ProductException {

	}

	private BigDecimal simulateApplyItemWiseDiscountToCart(Cart cart) {
		BigDecimal totalDiscountedPrice = BigDecimal.valueOf(cart.getTotalDiscountedPrice());
		BigDecimal totalPromotionDiscount = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getCartItems()) {
			BigDecimal price = BigDecimal.valueOf(cartItem.getDiscountedPrice());
			BigDecimal discountAmount = calculateDiscount(price, BigDecimal.valueOf(20.00), DiscountType.PERCENTAGE); // Example
																														// percentage
																														// discount
			BigDecimal discountedPrice = price.subtract(discountAmount);

			// Ensure the discounted price does not go below zero
			if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
				discountedPrice = BigDecimal.ZERO;
			}

			totalPromotionDiscount = totalPromotionDiscount.add(discountAmount);
			totalDiscountedPrice = totalDiscountedPrice.subtract(discountAmount);
		}

		return totalDiscountedPrice;
	}

	private BigDecimal simulateApplyAmountOffItemWiseDiscountToCart(Cart cart) {
		BigDecimal totalDiscountedPrice = BigDecimal.valueOf(cart.getTotalDiscountedPrice());
		BigDecimal totalPromotionDiscount = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getCartItems()) {
			BigDecimal price = BigDecimal.valueOf(cartItem.getDiscountedPrice());
			BigDecimal discountAmount = calculateDiscount(price, BigDecimal.valueOf(200.00), DiscountType.FLAT); // Example
																													// flat
																													// discount
			BigDecimal discountedPrice = price.subtract(discountAmount);

			// Ensure the discounted price does not go below zero
			if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
				discountedPrice = BigDecimal.ZERO;
			}

			totalPromotionDiscount = totalPromotionDiscount.add(discountAmount);
			totalDiscountedPrice = totalDiscountedPrice.subtract(discountAmount);
		}

		return totalDiscountedPrice;
	}

	public BigDecimal calculateDiscount(BigDecimal price, BigDecimal discountValue, DiscountType discountType) {
		BigDecimal discountAmount = BigDecimal.ZERO;

		switch (discountType) {
		case PERCENTAGE:
			// Calculate percentage discount
			discountAmount = price.multiply(discountValue).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			break;
		case FLAT:
			// Flat discount (fixed amount off)
			discountAmount = discountValue;
			break;
		default:
			throw new IllegalArgumentException("Unsupported discount type: " + discountType);
		}

		return discountAmount;
	}

	@Override
	public void applyBestTransactionWisePromotion(Long userId) throws ProductException {

	}

	// apply 5% OFF on if cartValue>=1500, transactionWise promotion
	@Override
//    public double applyFivePercentOFFTransactionWise(Long userId,boolean isForCheckBestPromotion){
//
//
//        System.out.println("\n\n\n\napplyFivePercentOFFTransactionWise");
//        //get cart data
//        Cart cart=cartRepository.findByUserId(userId);
//        //promotionCode
//        String promoCode="FIVEPERCENTOFF";
//
//        //get the list of all promotionCode currently applied on Cart
//        List<String>promocodeList=cart.getPromoCode();
//        if(promocodeList.isEmpty()){
//            promocodeList=new ArrayList<>();
//        }
//
//
//        if(cart!=null){
//
//            Promotion promotion=promotionRepository.findByPromotionCode(promoCode).orElseThrow(()->new RuntimeException("Promotion not found"+promoCode));
//
//            double totalAmount=0.0;
//            for(CartItem cartItem: cart.getCartItems()) {
//                totalAmount+=cartItem.getDiscountedPrice();
//               System.out.println("Price:"+cartItem.getDiscountedPrice()+" "+"Qant:"+cartItem.getQuantity());
//            }
//            System.out.println("Meet Condition:"+cart.getTotalDiscountedPrice()+" "+promotion.getMinOrderValue()+" "+"\nTotalAmount:"+totalAmount);
//
//
//
//            //cart size
//            int cartLength=cart.getCartItems().size();
//
//            if(cartLength>0) {
//
//                //totalDiscount
//                double totalDiscount=0.0;
//
//                if(totalAmount>=promotion.getMinOrderValue()){
//                    //totalDiscount on cart
//                    double totalDiscountOnCart=(totalAmount*(parseFloat(promotion.getDiscountValue())/100));
//                    System.out.println("totalDiscountOnCart:"+totalDiscountOnCart);
//
//                    //count total products in the cart
//                    int countProductInCart=0;
//                    for(CartItem cartItem:cart.getCartItems()){
//                        countProductInCart+=cartItem.getQuantity();
//                    }
//
//                    //splitDiscount on each product
//                    double splitDiscount=totalDiscountOnCart/countProductInCart;
//                    System.out.println("\nTotal5%Discount:"+totalDiscountOnCart+"countUniqueProduct:"+countProductInCart+"\nSplitDiscountOnEachProduct:"+splitDiscount);
//
//                    if(isForCheckBestPromotion){
//                        if(totalDiscountOnCart<=promotion.getMaxDiscountOnCart()){
//                            return totalAmount-totalDiscountOnCart;
//                        }else{
//                            return totalAmount-promotion.getMaxDiscountOnCart();
//                        }
//                    }else{
//
//                        for(CartItem cartItem: cart.getCartItems()){
//                            Long productId = cartItem.getProduct().getId();
//                            Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found with give id:"+productId));
//
//                            double productPrice=0.0;
//                            if(product.getPromotionalDiscountedPrice()!=null){
//                               productPrice=parseFloat(product.getPromotionalDiscountedPrice());
//                            }else{
//                                productPrice=cartItem.getProduct().getDiscountedPrice();
//                            }
//                            double productQuantity=cartItem.getQuantity();
//                            double totalProductPrice=(productPrice*productQuantity)-splitDiscount;
//                            cartItem.setDiscountedPrice((int) totalProductPrice);
//                            System.out.println("\nproductPrice:"+productPrice+" "+"productQuantity:"+productQuantity);
//                        }
//                        if(!promocodeList.contains(promoCode)){
//                            promocodeList.add(promoCode);
//                        }
//
//                        cartRepository.save(cart);
//                    }
//                }else{
//                    if(promocodeList.contains(promoCode)){
//                        promocodeList.remove(promoCode);
//                        cart.setPromoCode(promocodeList);
//                        cartRepository.save(cart);
//                    }
//                }
//            }
//
//        }
//        return -1.0;
//    }
	public double applyFivePercentOFFTransactionWise(Long userId, boolean isForCheckBestPromotion) {

		System.out.println("\n\n\n\napplyFivePercentOFFTransactionWise");
		// get cart data
		Cart cart = cartRepository.findByUserId(userId);
		// promotionCode
		String promoCode = "FIVEPERCENTOFF";

		// get the list of all promotionCode currently applied on Cart
		Map<String, Double> promocodeList = cart.getPromoCode();
		if (promocodeList.isEmpty()) {
			promocodeList = new HashMap<>();
		}

		if (cart != null) {

			Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
					.orElseThrow(() -> new RuntimeException("Promotion not found" + promoCode));

			double totalAmount = 0.0;
			for (CartItem cartItem : cart.getCartItems()) {
				if (!cartItem.getProduct().isEligibleForBogo()) {
					totalAmount += cartItem.getDiscountedPrice();
					System.out
							.println("Price:" + cartItem.getDiscountedPrice() + " " + "Qant:" + cartItem.getQuantity());
				}
			}
			System.out.println("Meet Condition:" + cart.getTotalDiscountedPrice() + " " + promotion.getMinOrderValue()
					+ " " + "\nTotalAmount:" + totalAmount);

			// cart size
			int cartLength = cart.getCartItems().size();

			if (cartLength > 0) {

				if (totalAmount >= promotion.getMinOrderValue()) {

					// totalDiscount on cart
					double totalDiscountOnCart = (totalAmount * (parseFloat(promotion.getDiscountValue()) / 100));
					System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);

					// Cap the discount to the max discount allowed
					totalDiscountOnCart = Math.min(totalDiscountOnCart, promotion.getMaxDiscountOnCart());

					// count total products in the cart
					int countProductInCart = 0;
					for (CartItem cartItem : cart.getCartItems()) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							countProductInCart += cartItem.getQuantity();
						}
					}

					// splitDiscount on each product
					double splitDiscount = totalDiscountOnCart / countProductInCart;
					System.out.println("\nTotal5%Discount:" + totalDiscountOnCart + "countUniqueProduct:"
							+ countProductInCart + "\nSplitDiscountOnEachProduct:" + splitDiscount);

					if (isForCheckBestPromotion) {
						if (totalDiscountOnCart <= promotion.getMaxDiscountOnCart()) {
							return totalAmount - totalDiscountOnCart;
						} else {
							return totalAmount - promotion.getMaxDiscountOnCart();
						}
					} else {

						for (CartItem cartItem : cart.getCartItems()) {
							Long productId = cartItem.getProduct().getId();
							Product product = productRepository.findById(productId).orElseThrow(
									() -> new RuntimeException("Product not found with give id:" + productId));
							if (!cartItem.getProduct().isEligibleForBogo()) {
								double productPrice = 0.0;
								if (product.getPromotionalDiscountedPrice() != null) {
									productPrice = parseFloat(product.getPromotionalDiscountedPrice());
								} else {
									productPrice = cartItem.getProduct().getDiscountedPrice();
								}

								parseFloat(promotion.getDiscountValue());
								double productQuantity = cartItem.getQuantity();
								double totalProductPrice = (productPrice * productQuantity)
										- (splitDiscount * productQuantity);
								cartItem.setDiscountedPrice((int) totalProductPrice);
								System.out.println("\nproductPrice:" + productPrice + " " + "productQuantity:"
										+ productQuantity + " \ntotalProductPrice:" + totalProductPrice + "\n");

								Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
								if (eligibleProductPromotion.isEmpty()) {
									eligibleProductPromotion = new HashMap<>();
								}
								// If it exists, update the existing discount value
								eligibleProductPromotion.put(promoCode, splitDiscount * productQuantity);
								if (eligibleProductPromotion.containsKey("FIVEHUNDREDOFF")) {
									eligibleProductPromotion.remove("FIVEHUNDREDOFF");
								} else if (eligibleProductPromotion.containsKey("BIRTH10PCT")) {
									eligibleProductPromotion.remove("BIRTH10PCT");
								} else if (eligibleProductPromotion.containsKey("ANNIVERSARY299")) {
									eligibleProductPromotion.remove("ANNIVERSARY299");
								}
								product.setAppliedPromotion(eligibleProductPromotion);
							}
						}
						// Ensure the discount does not exceed the maximum allowed discount
						if (totalDiscountOnCart > promotion.getMaxDiscountOnCart()) {
							totalDiscountOnCart = promotion.getMaxDiscountOnCart();
						}

						// Check if the promo code already exists in the promocodeList
						if (promocodeList.containsKey(promoCode)) {
							promocodeList.get(promoCode);
							promocodeList.put(promoCode, totalDiscountOnCart);
						} else {
							// If it doesn't exist, add the new promo code with the total discount
							promocodeList.put(promoCode, totalDiscountOnCart);
						}

						// Remove specific promo codes from the promocodeList
						if (promocodeList.containsKey("FIVEHUNDREDOFF")) {
							promocodeList.remove("FIVEHUNDREDOFF");
						}
						if (promocodeList.containsKey("BIRTH10PCT")) {
							promocodeList.remove("BIRTH10PCT");
						}
						if (promocodeList.containsKey("ANNIVERSARY299")) {
							promocodeList.remove("ANNIVERSARY299");
						}

// Set the updated promocodeList back to the cart
						cart.setPromoCode(promocodeList);

						System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);
						double calDist = 0.0;
						for (Map.Entry<String, Double> mp : promocodeList.entrySet()) {
							calDist += mp.getValue();
						}

						cart.setPromotion_discount(BigDecimal.valueOf(calDist));
						cartRepository.save(cart);
					}
				} else {
					if (promocodeList.containsKey(promoCode)) {
						// Get the discount value for the promo code
						double discountValue = promocodeList.get(promoCode);

						cart.setPromotion_discount(
								cart.getPromotion_discount().subtract(BigDecimal.valueOf(discountValue)));

//                        // Remove the promo code from the list
						promocodeList.remove(promoCode);

						// Update the cart with the modified promo code list
						cart.setPromoCode(promocodeList);
						cartRepository.save(cart);
					}

					for (CartItem cartItem : cart.getCartItems()) {
						Long productId = cartItem.getProduct().getId();
						Product product = productRepository.findById(productId)
								.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));
						if (!cartItem.getProduct().isEligibleForBogo()) {
							double productPrice = 0.0;
							if (product.getPromotionalDiscountedPrice() != null) {
								productPrice = parseFloat(product.getPromotionalDiscountedPrice());
							} else {
								productPrice = cartItem.getProduct().getDiscountedPrice();
							}
							double productQuantity = cartItem.getQuantity();
							double totalProductPrice = (productPrice * productQuantity);
							cartItem.setDiscountedPrice((int) totalProductPrice);
							System.out.println("\nproductPrice:" + productPrice + " " + "productQuantity:"
									+ productQuantity + " \ntotalProductPrice:" + totalProductPrice + "\n");

							Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();

							if (eligibleProductPromotion.containsKey(promoCode)) {
								eligibleProductPromotion.remove(promoCode);
								product.setAppliedPromotion(eligibleProductPromotion);
								productRepository.save(product);
							}
						}
					}
					cartRepository.save(cart);
				}
			} else {
				promocodeList.remove(promoCode);
				resetProductAppliedPromotionCode(userId);

			}

		}
		return -1.0;
	}

	// apply Rs.500 OFF on if cartValue>=1500, transactionWise promotion
	@Override
	public double applyFiveHundredOFFTransactionWise(Long userId, boolean isForCheckBestPromotion) {

		System.out.println("\n\n\n\napplyFiveHundredOFFTransactionWise");
		// get cart data
		Cart cart = cartRepository.findByUserId(userId);
		// promotionCode
		String promoCode = "FIVEHUNDREDOFF";

		// get the list of all promotionCode currently applied on Cart
		Map<String, Double> promocodeList = cart.getPromoCode();
		if (promocodeList.isEmpty()) {
			promocodeList = new HashMap<>();
		}

		if (cart != null) {

			Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
					.orElseThrow(() -> new RuntimeException("Promotion not found" + promoCode));

			double totalAmount = 0.0;
			for (CartItem cartItem : cart.getCartItems()) {
				if (!cartItem.getProduct().isEligibleForBogo()) {
					totalAmount += cartItem.getDiscountedPrice();
					System.out
							.println("Price:" + cartItem.getDiscountedPrice() + " " + "Qant:" + cartItem.getQuantity());
				}
			}
			System.out.println("Meet Condition:" + cart.getTotalDiscountedPrice() + " " + promotion.getMinOrderValue()
					+ " " + "\nTotalAmount:" + totalAmount);

			// cart size
			int cartLength = cart.getCartItems().size();

			if (cartLength > 0) {

				if (totalAmount >= promotion.getMinOrderValue() && totalAmount - promotion.getMinOrderValue() >= 0) {

					// totalDiscount on cart
					double totalDiscountOnCart = ((parseFloat(promotion.getDiscountValue())));
					System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);

					// Cap the discount to the max discount allowed
					totalDiscountOnCart = Math.min(totalDiscountOnCart, promotion.getMaxDiscountOnCart());

					// count total products in the cart
					int countProductInCart = 0;
					for (CartItem cartItem : cart.getCartItems()) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							countProductInCart += cartItem.getQuantity();
						}
					}

					// splitDiscount on each product
					double splitDiscount = totalDiscountOnCart / countProductInCart;
					System.out.println("\nTotal5OFFDiscount:" + totalDiscountOnCart + "OFFcountUniqueProduct:"
							+ countProductInCart + "\nOFFSplitDiscountOnEachProduct:" + splitDiscount);

					if (isForCheckBestPromotion) {
						if (totalDiscountOnCart <= promotion.getMaxDiscountOnCart()) {
							return totalAmount - totalDiscountOnCart;
						} else {
							return totalAmount - promotion.getMaxDiscountOnCart();
						}
					} else {

						for (CartItem cartItem : cart.getCartItems()) {
							Long productId = cartItem.getProduct().getId();
							Product product = productRepository.findById(productId).orElseThrow(
									() -> new RuntimeException("Product not found with give id:" + productId));
							if (!cartItem.getProduct().isEligibleForBogo()) {
								double productPrice = 0.0;
								if (product.getPromotionalDiscountedPrice() != null) {
									productPrice = parseFloat(product.getPromotionalDiscountedPrice());
								} else {
									productPrice = cartItem.getProduct().getDiscountedPrice();
								}

								double productQuantity = cartItem.getQuantity();
								double totalProductPrice = (productPrice * productQuantity)
										- (splitDiscount * productQuantity);
								cartItem.setDiscountedPrice((int) totalProductPrice);
								System.out.println("\nOFFproductPrice:" + productPrice + " " + "OFFproductQuantity:"
										+ productQuantity + " \nOFFtotalProductPrice:" + totalProductPrice + "\n");

								Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
								if (eligibleProductPromotion.isEmpty()) {
									eligibleProductPromotion = new HashMap<>();
								}
								// If it exists, update the existing discount value
								eligibleProductPromotion.put(promoCode, splitDiscount * productQuantity);
								if (eligibleProductPromotion.containsKey("FIVEPERCENTOFF")) {
									eligibleProductPromotion.remove("FIVEPERCENTOFF");
								} else if (eligibleProductPromotion.containsKey("BIRTH10PCT")) {
									eligibleProductPromotion.remove("BIRTH10PCT");
								} else if (eligibleProductPromotion.containsKey("ANNIVERSARY299")) {
									eligibleProductPromotion.remove("ANNIVERSARY299");
								}
								product.setAppliedPromotion(eligibleProductPromotion);
							}
						}
						// Ensure the discount does not exceed the maximum allowed discount
						if (totalDiscountOnCart > promotion.getMaxDiscountOnCart()) {
							totalDiscountOnCart = promotion.getMaxDiscountOnCart();
						}

						// Check if the promo code already exists in the promocodeList
						if (promocodeList.containsKey(promoCode)) {
							promocodeList.get(promoCode);
							promocodeList.put(promoCode, totalDiscountOnCart);
						} else {
							// If it doesn't exist, add the new promo code with the total discount
							promocodeList.put(promoCode, totalDiscountOnCart);
						}

						// Remove specific promo codes from the promocodeList
						if (promocodeList.containsKey("FIVEPERCENTOFF")) {
							promocodeList.remove("FIVEPERCENTOFF");
						}
						if (promocodeList.containsKey("BIRTH10PCT")) {
							promocodeList.remove("BIRTH10PCT");
						}
						if (promocodeList.containsKey("ANNIVERSARY299")) {
							promocodeList.remove("ANNIVERSARY299");
						}

						// Set the updated promocodeList back to the cart
						cart.setPromoCode(promocodeList);

						System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);
						double calDist = 0.0;
						for (Map.Entry<String, Double> mp : promocodeList.entrySet()) {
							calDist += mp.getValue();
						}

						cart.setPromotion_discount(BigDecimal.valueOf(calDist));
						cartRepository.save(cart);
					}
				} else {
					if (promocodeList.containsKey(promoCode)) {
						// Get the discount value for the promo code
						double discountValue = promocodeList.get(promoCode);

						cart.setPromotion_discount(
								cart.getPromotion_discount().subtract(BigDecimal.valueOf(discountValue)));

//                        // Remove the promo code from the list
						promocodeList.remove(promoCode);

						// Update the cart with the modified promo code list
						cart.setPromoCode(promocodeList);
						cartRepository.save(cart);
					}

					for (CartItem cartItem : cart.getCartItems()) {
						Long productId = cartItem.getProduct().getId();
						Product product = productRepository.findById(productId)
								.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));
						if (!cartItem.getProduct().isEligibleForBogo()) {
							double productPrice = 0.0;
							if (product.getPromotionalDiscountedPrice() != null) {
								productPrice = parseFloat(product.getPromotionalDiscountedPrice());
							} else {
								productPrice = cartItem.getProduct().getDiscountedPrice();
							}
							double productQuantity = cartItem.getQuantity();
							double totalProductPrice = (productPrice * productQuantity);
							cartItem.setDiscountedPrice((int) totalProductPrice);
							System.out.println("\nproductPrice:" + productPrice + " " + "productQuantity:"
									+ productQuantity + " \ntotalProductPrice:" + totalProductPrice + "\n");

							Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();

							if (eligibleProductPromotion.containsKey(promoCode)) {
								eligibleProductPromotion.remove(promoCode);
								product.setAppliedPromotion(eligibleProductPromotion);
								productRepository.save(product);
							}
						}
					}
					cartRepository.save(cart);
				}
			} else {
				promocodeList.remove(promoCode);
				resetProductAppliedPromotionCode(userId);
			}

		}
		return -1.0;
	}

	// First, calculate the total eligible amount.
	public double calculateCartTotalAmountWithPromotion(Cart cart, Set<Long> eligibleProductList,
			List<String> promocodeList, String promoCode) {
		double checkCartAmountWithPromotion = 0.0;
		// First, calculate the total eligible amount.
		for (CartItem cartItem : cart.getCartItems()) {
			Long productId = cartItem.getProduct().getId();
			if (eligibleProductList.contains(productId)) {
				// calculate the total price for the cart item with promotion
				if (promocodeList.contains(promoCode)) {
					checkCartAmountWithPromotion += cartItem.getDiscountedPrice();
				}
			}
		}
		return checkCartAmountWithPromotion;
	}

	// apply itemWise 200 OFF on selectedItems promotion when added in the cart
	@Override
	public double applyItemWiseTwoHundredOFFOnProduct(Long userId, boolean isForCheckBestPromotion) {
		System.out.println("\n\n\n\napplyItemWiseTwoHundredOFFOnProduct");
		// get cart data
		Cart cart = cartRepository.findByUserId(userId);
		// promotionCode
		String promoCode = "ITEM200OFF";

		if (cart != null) {
			// get eligible productList for this promotion
			Set<Long> eligibleProductList = promotionServiceImplementation
					.getEligibleProductIdForPromotion(promotionRepository.findByPromotionCode(promoCode).get());

			// get current Promotion
			Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
					.orElseThrow(() -> new RuntimeException("Promotion not exist in the DB:" + promoCode));

			// get the list of all promotionCode currently applied on Cart
			Map<String, Double> promocodeList = cart.getPromoCode();
			if (promocodeList.isEmpty()) {
				promocodeList = new HashMap<>();
			}

			// count eligible product
			int countEligibleProductCount = 0;
			for (CartItem cartItem : cart.getCartItems()) {
				Long productId = cartItem.getProduct().getId();
				if (eligibleProductList.contains(productId)) {
					if (!cartItem.getProduct().isEligibleForBogo()) {
						countEligibleProductCount++;
					}
				}
			}

			// split promotion on eligible product
			double splitWisePromotionOnEachProduct = parseFloat(promotion.getDiscountValue());
			System.out.println("\nsplitWisePromotionOnEachProduct:" + splitWisePromotionOnEachProduct
					+ "\ncountEligibleProductCount:" + countEligibleProductCount);
			// check cartAmount without promtion
			double checkCartAmountWithoutPromotion = 0.0;
			for (CartItem cartItem : cart.getCartItems()) {
				Long productId = cartItem.getProduct().getId();
				if (eligibleProductList.contains(productId)) {
					if (!cartItem.getProduct().isEligibleForBogo()) {
						checkCartAmountWithoutPromotion += (cartItem.getProduct().getDiscountedPrice()
								* cartItem.getQuantity());
					}
				}
			}
			System.out.println("\ncheckCartAmountWithoutPromotion:" + checkCartAmountWithoutPromotion);
			if (countEligibleProductCount > 0) {
				System.out.println("\n\n\nEntered in valid 200 OFF promotion");
				double totalDiscount = 0.0;
				int countEligibelItems = 0;
				for (CartItem cartItem : cart.getCartItems()) {
					Long productId = cartItem.getProduct().getId();
					Product product = productRepository.findById(productId)
							.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));

					if (eligibleProductList.contains(productId)) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							countEligibelItems++;
							double productPrice = (cartItem.getProduct().getDiscountedPrice());
							double productQuantity = (cartItem.getQuantity());
							totalDiscount += ((productPrice * productQuantity)
									- ((productPrice * productQuantity) - splitWisePromotionOnEachProduct));
							System.out.println("\ntotDiscount:" + totalDiscount + "\nprice:" + productPrice + "\nqnt:"
									+ productQuantity);

							if (!isForCheckBestPromotion) {
								Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
								if (eligibleProductPromotion.isEmpty()) {
									eligibleProductPromotion = new HashMap<>();
								}

								// If it exists, update the existing discount value
								eligibleProductPromotion.put(promoCode, Double.valueOf(promotion.getDiscountValue()));
								product.setAppliedPromotion(eligibleProductPromotion);

								cartItem.setDiscountedPrice(
										(int) ((productPrice * productQuantity) - splitWisePromotionOnEachProduct));
//                            product.setPromotionalDiscountedPrice(String.valueOf(Math.ceil(cartItem.getProduct().getDiscountedPrice()-splitWisePromotionOnEachProduct)));
								product.setPromotionalDiscountedPrice(String
										.valueOf((productPrice * productQuantity - splitWisePromotionOnEachProduct)
												/ productQuantity));
								productRepository.save(product);
							}
						}
					}
				}
//                if(!promocodeList.containsKey(promoCode) && !isForCheckBestPromotion){
//                    promocodeList.put(promoCode,totalDiscount);
//                    cart.setPromoCode(promocodeList);
//                }

				if (countEligibelItems > 0) {

					if (promocodeList.containsKey(promoCode) && !isForCheckBestPromotion) {
						// If it exists, update the existing discount value
						promocodeList.put(promoCode, totalDiscount);
						cart.setPromoCode(promocodeList);
					} else if (!promocodeList.containsKey(promoCode) && !isForCheckBestPromotion) {
						// If it doesn't exist, add the new promo code with the total discount
						promocodeList.put(promoCode, totalDiscount);
						cart.setPromoCode(promocodeList);
					}
					// if the function call only for check best promotion
					if (isForCheckBestPromotion) {
						if (totalDiscount <= promotion.getMaxDiscountOnCart()) {
							return (checkCartAmountWithoutPromotion - totalDiscount);
						} else {
							return (checkCartAmountWithoutPromotion - promotion.getMaxDiscountOnCart());
						}
					}
					if (totalDiscount <= promotion.getMaxDiscountOnCart()) {
						cart.setTotalDiscountedPrice((int) (Math.ceil(cart.getTotalDiscountedPrice() - totalDiscount)));
					} else {
						cart.setTotalDiscountedPrice(
								(int) (Math.ceil(cart.getTotalDiscountedPrice() - promotion.getMaxDiscountOnCart())));
					}
					cart.setPromotion_discount(BigDecimal.valueOf(totalDiscount));
				}
				cartRepository.save(cart);

			} else {
				if (promocodeList.containsKey(promoCode)) {
					promocodeList.remove(promoCode);
					cart.setPromoCode(promocodeList);
					cartRepository.save(cart);
				}

				if (cart.getPromoCode().isEmpty()) {
					cart.setPromotion_discount(BigDecimal.ZERO);
				}
				for (CartItem cartItem : cart.getCartItems()) {
					Long productId = cartItem.getProduct().getId();
					Product product = productRepository.findById(productId)
							.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));
					Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();

					if (eligibleProductPromotion.containsKey(promoCode)) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							eligibleProductPromotion.remove(promoCode);
							product.setAppliedPromotion(eligibleProductPromotion);
							productRepository.save(product);
						}
					}
				}
			}
		} else {

		}
		return 0.0;
	}

	// apply itemWise 20% OFF on selectedItems promotion when added in the cart
	@Override
	public double applyItemWiseTwentyPercentOFFOnProduct(Long userId, boolean isForCheckBestPromotion) {

		System.out.println("\n\n\n\napplyFiveHundredOFFTransactionWise");
		// get cart data
		Cart cart = cartRepository.findByUserId(userId);
		// promotionCode
		String promoCode = "SAVE20PCT";

		if (cart != null) {

			System.out.println("\n\n\nEntered in valid 20% OFF promotion");
			// get eligible productList for this promotion
			Set<Long> eligibleProductList = promotionServiceImplementation
					.getEligibleProductIdForPromotion(promotionRepository.findByPromotionCode(promoCode).get());
			System.out.println("\n\n\neligibleProductList " + eligibleProductList);
			// get current Promotion
			Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
					.orElseThrow(() -> new RuntimeException("Promotion not exist in the DB:" + promoCode));

			// get the list of all promotionCode currently applied on Cart
			Map<String, Double> promocodeList = cart.getPromoCode();
			if (promocodeList.isEmpty()) {
				promocodeList = new HashMap<>();
			}

			// count eligible product
			int countEligibleProductCount = 0;
			for (CartItem cartItem : cart.getCartItems()) {
				Long productId = cartItem.getProduct().getId();
				if (eligibleProductList.contains(productId)) {
					if (!cartItem.getProduct().isEligibleForBogo()) {
						countEligibleProductCount++;
					}
				}
			}

			if (countEligibleProductCount > 0) {
				// split promotion on eligible product
				double splitWisePromotionOnEachProduct = parseFloat(promotion.getDiscountValue());
				System.out.println("\nsplitWisePromotionOnEachProduct:" + splitWisePromotionOnEachProduct
						+ "\ncountEligibleProductCount:" + countEligibleProductCount);
				// check cartAmount without promtion
				double checkCartAmountWithoutPromotion = 0.0;
				for (CartItem cartItem : cart.getCartItems()) {
					Long productId = cartItem.getProduct().getId();
					if (eligibleProductList.contains(productId)) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							checkCartAmountWithoutPromotion += (cartItem.getProduct().getDiscountedPrice()
									* cartItem.getQuantity());
						}
					}
				}
				System.out.println("\ncheckCartAmountWithoutPromotion:" + checkCartAmountWithoutPromotion);
				double totalDiscount = 0.0;
				double countEligibleItems = 0;
				for (CartItem cartItem : cart.getCartItems()) {
					Long productId = cartItem.getProduct().getId();
					Product product = productRepository.findById(productId)
							.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));

					if (eligibleProductList.contains(productId)) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							countEligibleItems++;
							double productPrice = (cartItem.getProduct().getDiscountedPrice());
							double productQuantity = (cartItem.getQuantity());
							double itemDiscount = (productPrice * (splitWisePromotionOnEachProduct / 100));
							totalDiscount += itemDiscount;
							System.out.println("\ntotDiscount:" + totalDiscount + "\nprice:" + productPrice + "\nqnt:"
									+ productQuantity);

							if (!isForCheckBestPromotion) {
								Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
								if (eligibleProductPromotion.isEmpty()) {
									eligibleProductPromotion = new HashMap<>();
								}
								// If it exists, update the existing discount value

								eligibleProductPromotion.put(promoCode, itemDiscount);
								product.setAppliedPromotion(eligibleProductPromotion);

								cartItem.setDiscountedPrice((int) ((productPrice * productQuantity) - itemDiscount));
//                            product.setPromotionalDiscountedPrice(String.valueOf(Math.ceil(cartItem.getProduct().getDiscountedPrice()-itemDiscount)));
								product.setPromotionalDiscountedPrice(String
										.valueOf((productPrice * productQuantity - itemDiscount) / productQuantity));
								productRepository.save(product);
							}

						}
					}
				}
//                if(!promocodeList.containsKey(promoCode) && !isForCheckBestPromotion){
//                    promocodeList.put(promoCode,totalDiscount);
//                    cart.setPromoCode(promocodeList);
//                }

				if (countEligibleItems > 0) {
					if (promocodeList.containsKey(promoCode) && !isForCheckBestPromotion) {
						// If it exists, update the existing discount value
						promocodeList.put(promoCode, totalDiscount);
						cart.setPromoCode(promocodeList);
					} else if (!promocodeList.containsKey(promoCode) && !isForCheckBestPromotion) {
						// If it doesn't exist, add the new promo code with the total discount
						promocodeList.put(promoCode, totalDiscount);
						cart.setPromoCode(promocodeList);
					}

					// if the function call only for check best promotion
					if (isForCheckBestPromotion) {
						if (totalDiscount <= promotion.getMaxDiscountOnCart()) {
							return (checkCartAmountWithoutPromotion - totalDiscount);
						} else {
							return (checkCartAmountWithoutPromotion - promotion.getMaxDiscountOnCart());
						}
					}
					if (totalDiscount <= promotion.getMaxDiscountOnCart()) {
						cart.setTotalDiscountedPrice((int) (Math.ceil(cart.getTotalDiscountedPrice() - totalDiscount)));
					} else {
						cart.setTotalDiscountedPrice(
								(int) (Math.ceil(cart.getTotalDiscountedPrice() - promotion.getMaxDiscountOnCart())));
					}
					cart.setPromotion_discount(BigDecimal.valueOf(totalDiscount));
				}
				cartRepository.save(cart);
			} else {
				if (promocodeList.containsKey(promoCode)) {
					promocodeList.remove(promoCode);
					cart.setPromoCode(promocodeList);
					cartRepository.save(cart);
				}
				if (cart.getPromoCode().isEmpty()) {
					cart.setPromotion_discount(BigDecimal.ZERO);
				}

				for (CartItem cartItem : cart.getCartItems()) {
					Long productId = cartItem.getProduct().getId();
					Product product = productRepository.findById(productId)
							.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));
					Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();

					if (eligibleProductPromotion.containsKey(promoCode)) {
						if (!cartItem.getProduct().isEligibleForBogo()) {
							eligibleProductPromotion.remove(promoCode);
							product.setAppliedPromotion(eligibleProductPromotion);
							productRepository.save(product);
						}
					}
				}
			}

		} else {
			System.out.println("ELse me jaa rha h -> applyItemWiseTwoHundredOFFOnProduct");
		}
		return 0.0;
	}

	// apply anniversary promotion applied, if cartValue>=1500, transactionWise
	// promotion
	@Override
	public double anniversaryPromotionOnCart(Long userId, boolean isForCheckBestPromotion) {

		// get the user by userId
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("user not found with given userId:" + userId));
		// get user anniversaryDate
		LocalDate anniversaryDate = user.getAnniversaryDate();
		// get current date
		LocalDate currentDate = LocalDate.now();
		System.out.println("anniversaryDate:" + anniversaryDate + "\ncurrentDate:" + currentDate);
		if (anniversaryDate != null && anniversaryDate.equals(currentDate)) {
			System.out.println("\n\n\n\nanniversaryPromotionOnCart");
			// get cart data
			Cart cart = cartRepository.findByUserId(userId);
			// promotionCode
			String promoCode = "ANNIVERSARY299";

			// get the list of all promotionCode currently applied on Cart
			Map<String, Double> promocodeList = cart.getPromoCode();
			if (promocodeList.isEmpty()) {
				promocodeList = new HashMap<>();
			}

			if (cart != null) {

				Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
						.orElseThrow(() -> new RuntimeException("Promotion not found" + promoCode));

				double totalAmount = 0.0;
				for (CartItem cartItem : cart.getCartItems()) {
					if (!cartItem.getProduct().isEligibleForBogo()) {
						totalAmount += cartItem.getDiscountedPrice();
						System.out.println(
								"Price:" + cartItem.getDiscountedPrice() + " " + "Qant:" + cartItem.getQuantity());
					}
				}
				System.out.println("Meet Condition:" + cart.getTotalDiscountedPrice() + " "
						+ promotion.getMinOrderValue() + " " + "\nTotalAmount:" + totalAmount);

				// cart size
				int cartLength = cart.getCartItems().size();

				if (cartLength > 0) {

					if (totalAmount >= promotion.getMinOrderValue()
							&& totalAmount - promotion.getMinOrderValue() >= 0) {

						// totalDiscount on cart
						double totalDiscountOnCart = ((parseFloat(promotion.getDiscountValue())));
						System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);

						// Cap the discount to the max discount allowed
						totalDiscountOnCart = Math.min(totalDiscountOnCart, promotion.getMaxDiscountOnCart());

						// count total products in the cart
						int countProductInCart = 0;
						for (CartItem cartItem : cart.getCartItems()) {
							if (!cartItem.getProduct().isEligibleForBogo()) {
								countProductInCart += cartItem.getQuantity();
							}
						}

						// splitDiscount on each product
						double splitDiscount = totalDiscountOnCart / countProductInCart;
						System.out.println("\nTotal5OFFDiscount:" + totalDiscountOnCart + "OFFcountUniqueProduct:"
								+ countProductInCart + "\nOFFSplitDiscountOnEachProduct:" + splitDiscount);

						if (isForCheckBestPromotion) {
							if (totalDiscountOnCart <= promotion.getMaxDiscountOnCart()) {
								return totalAmount - totalDiscountOnCart;
							} else {
								return totalAmount - promotion.getMaxDiscountOnCart();
							}
						} else {

							for (CartItem cartItem : cart.getCartItems()) {
								Long productId = cartItem.getProduct().getId();
								Product product = productRepository.findById(productId).orElseThrow(
										() -> new RuntimeException("Product not found with give id:" + productId));
								if (!cartItem.getProduct().isEligibleForBogo()) {
									double productPrice = 0.0;
									if (product.getPromotionalDiscountedPrice() != null) {
										productPrice = parseFloat(product.getPromotionalDiscountedPrice());
									} else {
										productPrice = cartItem.getProduct().getDiscountedPrice();
									}

									double productQuantity = cartItem.getQuantity();
									double totalProductPrice = (productPrice * productQuantity)
											- (splitDiscount * productQuantity);
									cartItem.setDiscountedPrice((int) totalProductPrice);
									System.out.println("\nOFFproductPrice:" + productPrice + " " + "OFFproductQuantity:"
											+ productQuantity + " \nOFFtotalProductPrice:" + totalProductPrice + "\n");

									Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
									if (eligibleProductPromotion.isEmpty()) {
										eligibleProductPromotion = new HashMap<>();
									}
									// If it exists, update the existing discount value
									eligibleProductPromotion.put(promoCode, splitDiscount * productQuantity);

									if (eligibleProductPromotion.containsKey("FIVEPERCENTOFF")) {
										eligibleProductPromotion.remove("FIVEPERCENTOFF");
									} else if (eligibleProductPromotion.containsKey("FIVEHUNDREDOFF")) {
										eligibleProductPromotion.remove("FIVEHUNDREDOFF");
									} else if (eligibleProductPromotion.containsKey("BIRTH10PCT")) {
										eligibleProductPromotion.remove("BIRTH10PCT");
									}
									product.setAppliedPromotion(eligibleProductPromotion);
								}
							}
							// Ensure the discount does not exceed the maximum allowed discount
							if (totalDiscountOnCart > promotion.getMaxDiscountOnCart()) {
								totalDiscountOnCart = promotion.getMaxDiscountOnCart();
							}

							// Check if the promo code already exists in the promocodeList
							if (promocodeList.containsKey(promoCode)) {
								promocodeList.get(promoCode);
								promocodeList.put(promoCode, totalDiscountOnCart);
							} else {
								// If it doesn't exist, add the new promo code with the total discount
								promocodeList.put(promoCode, totalDiscountOnCart);
							}

							// Remove specific promo codes from the promocodeList
							if (promocodeList.containsKey("FIVEPERCENTOFF")) {
								promocodeList.remove("FIVEPERCENTOFF");
							}
							if (promocodeList.containsKey("FIVEHUNDREDOFF")) {
								promocodeList.remove("FIVEHUNDREDOFF");
							}
							if (promocodeList.containsKey("BIRTH10PCT")) {
								promocodeList.remove("BIRTH10PCT");
							}

							// Set the updated promocodeList back to the cart
							cart.setPromoCode(promocodeList);

							// Calculate the total discount from the updated promocodeList
							System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);
							double calDist = 0.0;
							for (Map.Entry<String, Double> mp : promocodeList.entrySet()) {
								calDist += mp.getValue();
							}

							// Set the promotion discount on the cart
							cart.setPromotion_discount(BigDecimal.valueOf(calDist));

							// Save the cart with the updated promocodeList and promotion discount
							cartRepository.save(cart);

						}
					} else {
						if (promocodeList.containsKey(promoCode)) {
							// Get the discount value for the promo code
							double discountValue = promocodeList.get(promoCode);

							cart.setPromotion_discount(
									cart.getPromotion_discount().subtract(BigDecimal.valueOf(discountValue)));

//                        // Remove the promo code from the list
							promocodeList.remove(promoCode);

							// Update the cart with the modified promo code list
							cart.setPromoCode(promocodeList);
							cartRepository.save(cart);
						}

						for (CartItem cartItem : cart.getCartItems()) {
							Long productId = cartItem.getProduct().getId();
							Product product = productRepository.findById(productId).orElseThrow(
									() -> new RuntimeException("Product not found with give id:" + productId));
							if (!cartItem.getProduct().isEligibleForBogo()) {
								double productPrice = 0.0;
								if (product.getPromotionalDiscountedPrice() != null) {
									productPrice = parseFloat(product.getPromotionalDiscountedPrice());
								} else {
									productPrice = cartItem.getProduct().getDiscountedPrice();
								}
								double productQuantity = cartItem.getQuantity();
								double totalProductPrice = (productPrice * productQuantity);
								cartItem.setDiscountedPrice((int) totalProductPrice);
								System.out.println("\nproductPrice:" + productPrice + " " + "productQuantity:"
										+ productQuantity + " \ntotalProductPrice:" + totalProductPrice + "\n");

								Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();

								if (eligibleProductPromotion.containsKey(promoCode)) {
									eligibleProductPromotion.remove(promoCode);
									product.setAppliedPromotion(eligibleProductPromotion);
									productRepository.save(product);
								}
							}
						}
						cartRepository.save(cart);
					}
				} else {
					promocodeList.remove(promoCode);
					resetProductAppliedPromotionCode(userId);
				}

			}
		}
		return -1.0;
	}

	// apply birthday promotion applied, if cartValue>=1500, transactionWise
	// promotion
	@Override
	public double birthdayPromotionOnCart(Long userId, boolean isForCheckBestPromotion) {

		// get the user by userId
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("user not found with given userId:" + userId));
		// get user anniversaryDate
		LocalDate dateOfBirth = user.getDateOfBirth();
		// get current date
		LocalDate currentDate = LocalDate.now();
		if (dateOfBirth != null && dateOfBirth.equals(currentDate)) {
			System.out.println("\n\n\n\napplyFivePercentOFFTransactionWise");
			// get cart data
			Cart cart = cartRepository.findByUserId(userId);
			// promotionCode
			String promoCode = "FIVEPERCENTOFF";

			// get the list of all promotionCode currently applied on Cart
			Map<String, Double> promocodeList = cart.getPromoCode();
			if (promocodeList.isEmpty()) {
				promocodeList = new HashMap<>();
			}

			if (cart != null) {

				Promotion promotion = promotionRepository.findByPromotionCode(promoCode)
						.orElseThrow(() -> new RuntimeException("Promotion not found" + promoCode));

				double totalAmount = 0.0;
				for (CartItem cartItem : cart.getCartItems()) {
					if (!cartItem.getProduct().isEligibleForBogo()) {
						totalAmount += cartItem.getDiscountedPrice();
						System.out.println(
								"Price:" + cartItem.getDiscountedPrice() + " " + "Qant:" + cartItem.getQuantity());
					}
				}
				System.out.println("Meet Condition:" + cart.getTotalDiscountedPrice() + " "
						+ promotion.getMinOrderValue() + " " + "\nTotalAmount:" + totalAmount);

				// cart size
				int cartLength = cart.getCartItems().size();

				if (cartLength > 0) {

					if (totalAmount >= promotion.getMinOrderValue()) {

						// totalDiscount on cart
						double totalDiscountOnCart = (totalAmount * (parseFloat(promotion.getDiscountValue()) / 100));
						System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);

						// Cap the discount to the max discount allowed
						totalDiscountOnCart = Math.min(totalDiscountOnCart, promotion.getMaxDiscountOnCart());

						// count total products in the cart
						int countProductInCart = 0;
						for (CartItem cartItem : cart.getCartItems()) {
							if (!cartItem.getProduct().isEligibleForBogo()) {
								countProductInCart += cartItem.getQuantity();
							}
						}

						// splitDiscount on each product
						double splitDiscount = totalDiscountOnCart / countProductInCart;
						System.out.println("\nTotal5%Discount:" + totalDiscountOnCart + "countUniqueProduct:"
								+ countProductInCart + "\nSplitDiscountOnEachProduct:" + splitDiscount);

						if (isForCheckBestPromotion) {
							if (totalDiscountOnCart <= promotion.getMaxDiscountOnCart()) {
								return totalAmount - totalDiscountOnCart;
							} else {
								return totalAmount - promotion.getMaxDiscountOnCart();
							}
						} else {

							for (CartItem cartItem : cart.getCartItems()) {
								Long productId = cartItem.getProduct().getId();
								Product product = productRepository.findById(productId).orElseThrow(
										() -> new RuntimeException("Product not found with give id:" + productId));
								if (!cartItem.getProduct().isEligibleForBogo()) {
									double productPrice = 0.0;
									if (product.getPromotionalDiscountedPrice() != null) {
										productPrice = parseFloat(product.getPromotionalDiscountedPrice());
									} else {
										productPrice = cartItem.getProduct().getDiscountedPrice();
									}

									parseFloat(promotion.getDiscountValue());
									double productQuantity = cartItem.getQuantity();
									double totalProductPrice = (productPrice * productQuantity)
											- (splitDiscount * productQuantity);
									cartItem.setDiscountedPrice((int) totalProductPrice);
									System.out.println("\nproductPrice:" + productPrice + " " + "productQuantity:"
											+ productQuantity + " \ntotalProductPrice:" + totalProductPrice + "\n");

									Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
									if (eligibleProductPromotion.isEmpty()) {
										eligibleProductPromotion = new HashMap<>();
									}
									// If it exists, update the existing discount value
									eligibleProductPromotion.put(promoCode, splitDiscount * productQuantity);

									if (eligibleProductPromotion.containsKey("FIVEPERCENTOFF")) {
										eligibleProductPromotion.remove("FIVEPERCENTOFF");
									} else if (eligibleProductPromotion.containsKey("FIVEHUNDREDOFF")) {
										eligibleProductPromotion.remove("FIVEHUNDREDOFF");
									} else if (eligibleProductPromotion.containsKey("ANNIVERSARY299")) {
										eligibleProductPromotion.remove("ANNIVERSARY299");
									}
									product.setAppliedPromotion(eligibleProductPromotion);
								}
							}
							// Ensure the discount does not exceed the maximum allowed discount
							if (totalDiscountOnCart > promotion.getMaxDiscountOnCart()) {
								totalDiscountOnCart = promotion.getMaxDiscountOnCart();
							}

							// Check if the promo code already exists in the promocodeList
							if (promocodeList.containsKey(promoCode)) {
								promocodeList.get(promoCode);
								promocodeList.put(promoCode, totalDiscountOnCart);
							} else {
								// If it doesn't exist, add the new promo code with the total discount
								promocodeList.put(promoCode, totalDiscountOnCart);
							}

							// Remove specific promo codes from the promocodeList
							if (promocodeList.containsKey("FIVEPERCENTOFF")) {
								promocodeList.remove("FIVEPERCENTOFF");
							}
							if (promocodeList.containsKey("FIVEHUNDREDOFF")) {
								promocodeList.remove("FIVEHUNDREDOFF");
							}
							if (promocodeList.containsKey("ANNIVERSARY299")) {
								promocodeList.remove("ANNIVERSARY299");
							}

// Set the updated promocodeList back to the cart
							cart.setPromoCode(promocodeList);

							System.out.println("totalDiscountOnCart:" + totalDiscountOnCart);
							double calDist = 0.0;
							for (Map.Entry<String, Double> mp : promocodeList.entrySet()) {
								calDist += mp.getValue();
							}

							cart.setPromotion_discount(BigDecimal.valueOf(calDist));
							cartRepository.save(cart);
						}
					} else {
						if (promocodeList.containsKey(promoCode)) {
							// Get the discount value for the promo code
							double discountValue = promocodeList.get(promoCode);

							cart.setPromotion_discount(
									cart.getPromotion_discount().subtract(BigDecimal.valueOf(discountValue)));

//                        // Remove the promo code from the list
							promocodeList.remove(promoCode);

							// Update the cart with the modified promo code list
							cart.setPromoCode(promocodeList);
							cartRepository.save(cart);
						}

						for (CartItem cartItem : cart.getCartItems()) {
							Long productId = cartItem.getProduct().getId();
							Product product = productRepository.findById(productId).orElseThrow(
									() -> new RuntimeException("Product not found with give id:" + productId));
							if (!cartItem.getProduct().isEligibleForBogo()) {
								double productPrice = 0.0;
								if (product.getPromotionalDiscountedPrice() != null) {
									productPrice = parseFloat(product.getPromotionalDiscountedPrice());
								} else {
									productPrice = cartItem.getProduct().getDiscountedPrice();
								}
								double productQuantity = cartItem.getQuantity();
								double totalProductPrice = (productPrice * productQuantity);
								cartItem.setDiscountedPrice((int) totalProductPrice);
								System.out.println("\nproductPrice:" + productPrice + " " + "productQuantity:"
										+ productQuantity + " \ntotalProductPrice:" + totalProductPrice + "\n");

								Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();

								if (eligibleProductPromotion.containsKey(promoCode)) {
									eligibleProductPromotion.remove(promoCode);
									product.setAppliedPromotion(eligibleProductPromotion);
									productRepository.save(product);
								}
							}
						}
						cartRepository.save(cart);
					}
				} else {
					promocodeList.remove(promoCode);
					resetProductAppliedPromotionCode(userId);

				}

			}
		}
		return -1.0;
	}

	// boGo promotion on selected products
	@Override
	public double createBoGoPromotion(Long userId, boolean isForCheckBestPromotion) {
		System.out.println("\n\nEntered in the BOGO Promotion");
		// Get cart data
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			throw new RuntimeException("Cart not found for user: " + userId);
		}

		// Promotion code
		String promoCode = "BOGO";

		// Get the list of all promotion codes currently applied on the cart
		Map<String, Double> promocodeList = cart.getPromoCode();
		if (promocodeList == null || promocodeList.isEmpty()) {
			promocodeList = new HashMap<>();
		}

		promotionRepository.findByPromotionCode(promoCode)
				.orElseThrow(() -> new RuntimeException("Promotion not found: " + promoCode));

		double totalAmount = 0.0;

		for (CartItem cartItem : cart.getCartItems()) {
			if (cartItem.getProduct().isEligibleForBogo()) {
				totalAmount += cartItem.getDiscountedPrice();
				System.out.println("Price:" + cartItem.getDiscountedPrice() + " " + "Qant:" + cartItem.getQuantity());
			}
		}

		System.out.println("\nTotalAmountBOGO:" + totalAmount);

		if (!cart.getCartItems().isEmpty()) {
			double totalDiscount = 0.0;

			// check does any BOgO product in cart
			boolean foundBoGoProduct = false;

			for (CartItem cartItem : cart.getCartItems()) {
				Long productId = cartItem.getProduct().getId();
				Product product = productRepository.findById(productId)
						.orElseThrow(() -> new RuntimeException("Product not found with give id:" + productId));

				double productPrice = cartItem.getProduct().getDiscountedPrice();
				double productQuantity = 0.0;
				if (cartItem.getQuantity() == 1) {
					productQuantity = cartItem.getQuantity() + 1;
				} else {
					productQuantity = cartItem.getQuantity();
				}

				double totalProductPrice = 0.0;
				if (productQuantity % 2 != 0) {
					totalProductPrice = (productPrice * Math.ceil(productQuantity / 2));
				} else {
					totalProductPrice = ((productPrice * productQuantity) / 2);
				}

				if (cartItem.getProduct().isEligibleForBogo()) {
					foundBoGoProduct = true;
					double discountOnProduct = 0.0;

					double discountQnt = 0.0;
					if (productQuantity % 2 == 0) {
						discountQnt = Math.floor(productQuantity / 2);
						discountOnProduct = (productPrice * productQuantity) - productPrice * discountQnt;
					} else {
						discountQnt = Math.ceil(productQuantity / 2);
						discountOnProduct = ((productPrice * productQuantity) - (productPrice * discountQnt));
					}

					totalDiscount += discountOnProduct;
					cartItem.setDiscountedPrice((int) totalProductPrice);
					cartItem.setQuantity((int) productQuantity);
					Map<String, Double> eligibleProductPromotion = product.getAppliedPromotion();
					if (eligibleProductPromotion.isEmpty()) {
						eligibleProductPromotion = new HashMap<>();
					}

					// If it exists, update the existing discount value
					eligibleProductPromotion.put(promoCode, discountOnProduct);
					product.setAppliedPromotion(eligibleProductPromotion);

					// Print all variables in one line
					System.out.println("\n\nDiscount on Product: " + discountOnProduct + ", \nPrice of Product: "
							+ productPrice + ", \nproductQuantity:" + productQuantity + ", \ntotalProductPrice:"
							+ totalProductPrice + "\ndiscountQnt:" + discountQnt);
				}
			}

			// add promoCode in the productAppliedPromotions
			Map<String, Double> cartPromoCodeList = cart.getPromoCode();
			if (cartPromoCodeList == null || cartPromoCodeList.isEmpty()) {
				cartPromoCodeList = new HashMap<>();
			}

			if (foundBoGoProduct) {
				// Set promoCode in the cart
				System.out.println("Applied PromoCode:" + promoCode + "\nApplied Discount:" + totalDiscount);
				cartPromoCodeList.put(promoCode, totalDiscount);
				cart.setPromoCode(cartPromoCodeList);
			} else {
				cartPromoCodeList.remove(promoCode);
			}

			// add total promotionDiscount
			cart.setPromotion_discount(cart.getPromotion_discount().add(BigDecimal.valueOf(totalDiscount)));
			// Save the updated cart
			cartRepository.save(cart);

		} else {
			if (promocodeList.containsKey(promoCode)) {

				// remove total promotionDiscount
				cart.setPromotion_discount(
						cart.getPromotion_discount().subtract(BigDecimal.valueOf(promocodeList.get(promoCode))));

				promocodeList.remove(promoCode);
				cart.setPromoCode(promocodeList);
			}
			cartRepository.save(cart);
		}
		return -1.0;
	}

	// reset all product promoCode
	void resetProductAppliedPromotionCode(Long userId) {
		// get cart data
		Cart cart = cartRepository.findByUserId(userId);
		if (cart != null) {
			cart.setPromotion_discount(BigDecimal.ZERO);
		}
		for (Product product : productRepository.findAll()) {
			product.getId();
			product.getAppliedPromotion().clear();
			productRepository.save(product);
		}
	}

	// apply Best Promotion on cart
	@Override
	public void applyBestPromotionOnCart(Long userId) {
		// check the best promotion transactionWise
		boolean isForCheckBestPromotion = true;

		// check the best promotion itemWise
		System.out.println("applyItemWiseTwentyPercentOFFOnProduct:"
				+ applyItemWiseTwentyPercentOFFOnProduct(userId, isForCheckBestPromotion));
		System.out.println("applyItemWiseTwoHundredOFFOnProduct:"
				+ applyItemWiseTwoHundredOFFOnProduct(userId, isForCheckBestPromotion));
		if (applyItemWiseTwentyPercentOFFOnProduct(userId,
				isForCheckBestPromotion) < applyItemWiseTwoHundredOFFOnProduct(userId, isForCheckBestPromotion)) {
			applyItemWiseTwentyPercentOFFOnProduct(userId, false);
		} else {
			applyItemWiseTwoHundredOFFOnProduct(userId, false);
		}

		System.out.println("applyFivePercentOFFTransactionWise"
				+ applyFivePercentOFFTransactionWise(userId, isForCheckBestPromotion));
		System.out.println("applyFiveHundredOFFTransactionWise"
				+ applyFiveHundredOFFTransactionWise(userId, isForCheckBestPromotion));

		double fivePercentOff = applyFivePercentOFFTransactionWise(userId, isForCheckBestPromotion);
		double fiveHundredOff = applyFiveHundredOFFTransactionWise(userId, isForCheckBestPromotion);
		double anniversaryOff = anniversaryPromotionOnCart(userId, isForCheckBestPromotion);
		double birthdayOff = birthdayPromotionOnCart(userId, isForCheckBestPromotion);

		System.out.println("Five Percent Off: " + fivePercentOff);
		System.out.println("Five Hundred Off: " + fiveHundredOff);
		System.out.println("Anniversary Off: " + anniversaryOff);
		System.out.println("Birthday Off: " + birthdayOff);

		if (fivePercentOff > 0 && (fivePercentOff <= fiveHundredOff || fiveHundredOff == -1.0)
				&& (fivePercentOff <= anniversaryOff || anniversaryOff == -1.0)
				&& (fivePercentOff <= birthdayOff || birthdayOff == -1.0)) {
			System.out.println("\n\nfivePercentOffApplied");
			applyFivePercentOFFTransactionWise(userId, false);

		} else if (fiveHundredOff > 0 && (fiveHundredOff <= fivePercentOff || fivePercentOff == -1.0)
				&& (fiveHundredOff <= anniversaryOff || anniversaryOff == -1.0)
				&& (fiveHundredOff <= birthdayOff || birthdayOff == -1.0)) {
			System.out.println("\n\nfiveHundredOffApplied");
			applyFiveHundredOFFTransactionWise(userId, false);
		} else if (anniversaryOff > 0 && (anniversaryOff <= fivePercentOff || fivePercentOff == -1.0)
				&& (anniversaryOff <= fiveHundredOff || fiveHundredOff == -1.0)
				&& (anniversaryOff <= birthdayOff || birthdayOff == -1.0)) {
			anniversaryPromotionOnCart(userId, false);
		} else if (birthdayOff > 0 && (birthdayOff <= fivePercentOff || fivePercentOff == -1.0)
				&& (birthdayOff <= fiveHundredOff || fiveHundredOff == -1.0)
				&& (birthdayOff <= anniversaryOff || anniversaryOff == -1.0)) {
			birthdayPromotionOnCart(userId, false);
		}

		// apply BoGo Promotion on cart
		createBoGoPromotion(userId, isForCheckBestPromotion);
	}
}