package com.deepanshu.controller;

import java.util.List;
import java.util.Map;

import com.deepanshu.request.PromotionRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Product;
import com.deepanshu.service.ProductService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://localhost:8081")
public class UserProductController {

    private ProductService productService;

    public UserProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
                                                                      @RequestParam List<String> color, @RequestParam List<String> size, @RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort,
                                                                      @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize,
                                                                      @RequestParam(required = false) String country,
                                                                      @RequestParam(required = false) String wearType,
                                                                      @RequestParam(required = false) String fabric,
                                                                      @RequestParam(required = false) String sleeves,
                                                                      @RequestParam(required = false) String fit,
                                                                      @RequestParam(required = false) String materialCare,
                                                                      @RequestParam(required = false) String productCode,
                                                                      @RequestParam(required = false) String seller) {


        Page<Product> res = productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize,
                country, wearType, fabric, sleeves, fit, materialCare, productCode, seller);

        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

    }


    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {

        Product product = productService.findProductById(productId);

        return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q) {

        List<Product> products = productService.searchProduct(q);

        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);

    }

    //fetch product brandName list,colors list,price range,sellerName List based on filterName
    @PostMapping("/products/filter")
    public ResponseEntity<List<String>> filterProductsByCategoryAttributeHandler( @RequestBody  List<Product>products,
                                                                                  @RequestParam  String category
    ) {

        // Call the filterOnPLP method you created
        List<String> filteredData = productService.filterOnPLP(products,category);

        // If filtered data is empty, return a 404 NOT FOUND
        if (filteredData == null || filteredData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredData, HttpStatus.OK);
    }

    // New endpoint to filter products based on multiple attributes
    @PostMapping("/products/multiFilter")
    public ResponseEntity<List<Product>> filterProductsHandler(
            @RequestParam String category, @RequestBody(required = false) Map<String,List<String>> filterData)
    {
        List<String> brand=filterData.get("brand");
        List<String> color=filterData.get("color");
        List<String> seller=filterData.get("seller");
        List<Product> filteredProducts = productService.filterProductBasedOnMultiFilter(category, brand, color, seller);

        // If filtered products is empty, return a 404 NOT FOUND
        if (filteredProducts == null || filteredProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }

    // Filter products based on brand and category using path variables
    @GetMapping("/products/filterByBrand/brandName={brandName}/categoryName={categoryName}")
    public List<Product> filterProductBasedOnBrand(
            @PathVariable String brandName,
            @PathVariable String categoryName) {

        return productService.filterProductBasedOnBrand(brandName, categoryName);
    }


    //apply BOGO on productId
    @PostMapping("/applyBogo")
    public String applyBogo(@RequestBody PromotionRequest promotionRequest){
        return productService.applyBogo(promotionRequest);
    }

}
