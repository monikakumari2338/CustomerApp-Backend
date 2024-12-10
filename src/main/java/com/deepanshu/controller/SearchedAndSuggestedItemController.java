package com.deepanshu.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.modal.Product;
import com.deepanshu.service.SearchedIAndSuggestedtemService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://localhost:8081")
@SecurityRequirement(name = "bearerAuth")
public class SearchedAndSuggestedItemController {

	@Autowired
	private SearchedIAndSuggestedtemService searchedItemService;

	@GetMapping("/searcheditem/add/{param}/{userId}")
	public ResponseEntity<String> addSearchedItems(@PathVariable String param, @PathVariable Long userId) {

		String response = searchedItemService.saveSearchedItems(param, userId);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}

	@GetMapping("/getSuggestedItems/{userId}")
	public ResponseEntity<List<Product>> getSuggestedItems(@PathVariable Long userId) {

		List<Product> items = searchedItemService.getSuggestedProducts(userId);
		return new ResponseEntity<List<Product>>(items, HttpStatus.OK);

	}

}