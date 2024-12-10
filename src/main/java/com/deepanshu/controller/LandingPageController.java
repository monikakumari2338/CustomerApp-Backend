package com.deepanshu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.Dto.LandingPageDto;
import com.deepanshu.service.LandingPageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://localhost:8081")
@SecurityRequirement(name = "bearerAuth")
public class LandingPageController {

	@Autowired
	private LandingPageService landingPageService;

	@PostMapping("/createContinueShopping/{userId}/{productId}/{sku}")
	public ResponseEntity<String> createContinueShopping(@PathVariable Long userId, @PathVariable Long productId,
			@PathVariable String sku) {

		String response = landingPageService.createContinueShopping(userId, productId, sku);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@GetMapping("/getContinueShopping/{userId}")
	public ResponseEntity<List<LandingPageDto>> getContinueShopping(@PathVariable Long userId) {

		List<LandingPageDto> items = landingPageService.getContinueShoppingProducts(userId);
		return new ResponseEntity<List<LandingPageDto>>(items, HttpStatus.OK);

	}

	@GetMapping("/getFeaturedProducts")
	public ResponseEntity<List<LandingPageDto>> getFeaturedProducts() {

		List<LandingPageDto> items = landingPageService.getFeaturedProducts();
		return new ResponseEntity<List<LandingPageDto>>(items, HttpStatus.OK);

	}

	@GetMapping("/getTopRatedProducts")
	public ResponseEntity<List<LandingPageDto>> getTopRatedProducts() {

		List<LandingPageDto> items = landingPageService.getTopRatedPrpducts();
		return new ResponseEntity<List<LandingPageDto>>(items, HttpStatus.OK);

	}

}