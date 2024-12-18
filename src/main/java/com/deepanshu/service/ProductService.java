package com.deepanshu.service;

import java.io.IOException;
import java.util.List;

import com.deepanshu.request.PromotionRequest;
import org.springframework.data.domain.Page;

import com.deepanshu.Dto.PdpDto;
import com.deepanshu.Dto.PlpCardDto;
import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Product;
import com.deepanshu.request.CreateProductRequest;

public interface ProductService {
	public Product createProduct(CreateProductRequest req) throws ProductException, IOException;

	public String deleteProduct(Long productId) throws ProductException;

	public Product updateProduct(Long productId, Product product) throws ProductException;

	public List<Product> getAllProducts();

	public Product findProductById(Long id) throws ProductException;

	public List<Product> findProductByCategory(String category);

	public List<Product> searchProduct(String query);

	public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize,
			String country, String wearType, String fabric, String sleeves, String fit, String materialCare,
			String productCode, String seller);

	Product getProductById(Long newProductId);

	// PdpDto getPdpProductById(Long newProductId);

	Product save(Product product);

	// filter the data based on the topLevelCategory selected
	List<PlpCardDto> getTopCategoryWise(String category);

	// filter the data based on the secondLevelCategory selected
	List<Product> getSecondCategoryWise(String category);

	// filter the data based on the thirdLevelCategory selected
	List<Product> getThirdCategoryWise(String category);

	// sort the product based on price low to high
	List<Product> sortProductBasedOnLowPrice(List<Product> allProducts);

	// sort the product based on price high to low
	List<Product> sortProductBasedOnHighPrice(List<Product> allProducts);

	// sort the product based on high discount
	List<Product> sortProductBasedOnHighDiscount(List<Product> allProducts);

	// sort the product based on new arrival
	List<Product> sortProductBasedOnNewArrival(List<Product> allProducts);

	// sort the product based on rating
	List<Product> sortProductBasedOnRating(List<Product> allProducts);

	// fetch product brandName list,colors list,price range,sellerName List based on
	// filterName
	List<String> filterOnPLP(List<Product> products, String category);

	// filter products based on multiple filters
	List<Product> filterProductBasedOnMultiFilter(String category, List<String> brand, List<String> color,
			List<String> seller);

	// search all pincode by productId
	List<String> getAllPincodeByProductId(Long productId) throws Exception;

	// search product availability by pincode
	Boolean searchProductAvailabiltyByPincode(Long productId, String pincode) throws Exception;

	// filter product based on brand with category
	List<Product> filterProductBasedOnBrand(String brand, String brandCategory);

	// apply BOGO
	String applyBogo(PromotionRequest promotionRequest);

	List<PlpCardDto> searchProductBySearchBar(String query);

	PdpDto getPdpProductById(Long newProductId);

}
