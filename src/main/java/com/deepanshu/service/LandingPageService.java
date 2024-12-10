package com.deepanshu.service;

import java.util.List;

import com.deepanshu.Dto.LandingPageDto;

public interface LandingPageService {

	String createContinueShopping(Long userId, Long productId, String sku);

	List<LandingPageDto> getContinueShoppingProducts(Long userId);

	List<LandingPageDto> getFeaturedProducts();

	List<LandingPageDto> getTopRatedPrpducts();
}