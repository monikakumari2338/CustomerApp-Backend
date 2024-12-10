package com.deepanshu.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Product;
import com.deepanshu.request.CreateProductRequest;
import com.deepanshu.response.ApiResponse;
import com.deepanshu.service.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "https://localhost:8081")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

	@Autowired
	private ProductService productService;

	public AdminProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/")
	public ResponseEntity<Product> createProductHandler(@RequestBody CreateProductRequest req)
			throws ProductException, IOException {
		Product createdProduct = productService.createProduct(req);
		return new ResponseEntity<Product>(createdProduct, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException {
		System.out.println("delete product controller .... ");
		String msg = productService.deleteProduct(productId);
		System.out.println("delete product controller .... msg " + msg);
		ApiResponse res = new ApiResponse(msg, true);
		return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Product>> findAllProduct() {
		List<Product> products = productService.getAllProducts();
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}

	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProductHandler(@RequestBody Product req, @PathVariable Long productId)
			throws ProductException {
		Product updatedProduct = productService.updateProduct(productId, req);
		return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
	}

	@PostMapping("/creates")
	public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] reqs)
			throws ProductException, IOException {
		for (CreateProductRequest product : reqs) {
			productService.createProduct(product);
		}

		ApiResponse res = new ApiResponse("products created successfully", true);
		return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
	}

	// filter the data based on the topLevelCategory selected
	@GetMapping("/getProductByTopCategory")
	public List<Product> getTopProductByCategory(@RequestParam String category) {
		try {
			return productService.getTopCategoryWise(category);
		} catch (Exception e) {
			return null;
		}
	}

	// filter the data based on the secondLevelCategory selected
	@GetMapping("/getProductBySecondCategory")
	public List<Product> getSecondProductByCategory(@RequestParam String category) {
		try {
			return productService.getSecondCategoryWise(category);
		} catch (Exception e) {
			return null;
		}
	}

	// filter the data based on the thirdLevelCategory selected
	@GetMapping("/getProductByThirdCategory")
	public List<Product> getThirdProductByCategory(@RequestParam String category) {
		try {
			return productService.getThirdCategoryWise(category);
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping("/sortByLowPrice")
	public ResponseEntity<List<Product>> sortProductByLowPrice(@RequestBody List<Product> products) {
		try {
			List<Product> sortedProducts = productService.sortProductBasedOnLowPrice(products);
			return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sortByHighPrice")
	public ResponseEntity<List<Product>> sortProductByHighPrice(@RequestBody List<Product> products) {
		try {
			List<Product> sortedProducts = productService.sortProductBasedOnHighPrice(products);
			return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sortByHighDiscount")
	public ResponseEntity<List<Product>> sortProductByHighDiscount(@RequestBody List<Product> products) {
		try {
			List<Product> sortedProducts = productService.sortProductBasedOnHighDiscount(products);
			return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sortByNewArrival")
	public ResponseEntity<List<Product>> sortProductByNewArrival(@RequestBody List<Product> products) {
		try {
			List<Product> sortedProducts = productService.sortProductBasedOnNewArrival(products);
			return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/sortByRating")
	public ResponseEntity<List<Product>> sortProductByRating(@RequestBody List<Product> products) {
		try {
			List<Product> sortedProducts = productService.sortProductBasedOnRating(products);
			return new ResponseEntity<>(sortedProducts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// search all pincode by productId
	@GetMapping("/getAllPincodeByProductId/productId={productId}")
	public List<String> getAllPincodeByProductId(@PathVariable Long productId) throws Exception {
		return productService.getAllPincodeByProductId(productId);
	}

	// search product availability by pincode
	@GetMapping("/searchProductAvailable/productId={productId}/pincode={pincode}")
	public Boolean searchProductAvailabiltyByPincode(@PathVariable Long productId, @PathVariable String pincode)
			throws Exception {
		return productService.searchProductAvailabiltyByPincode(productId, pincode);
	}

}
