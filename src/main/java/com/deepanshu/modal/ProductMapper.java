package com.deepanshu.modal;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductElasticSearch toElasticSearch(Product product) {
        // Create a new ProductElasticSearch object and map the fields from Product
        return new ProductElasticSearch(
                product.getId().toString(), // Assuming id is Long in Product, converted to String
                product.getTitle(),         // Mapping the name
                product.getDescription(),   // Mapping the description
                product.getPrice(),         // Mapping the price
                product.getDiscountedPrice(), // Mapping the discountedPrice
                product.getBrand(),         // Mapping the brand
                product.getWearType(),      // Mapping the wearType
                product.getFabric(),        // Mapping the fabric
                product.getFit(),           // Mapping the fit
                product.getMaterialCare(),  // Mapping the materialCare
                product.getProductCode(),   // Mapping the productCode
                product.getSeller()         // Mapping the seller
                // Omitting topLevelCategory, secondLevelCategory, thirdLevelCategory, and images
        );
    }

    public static List<ProductElasticSearch> toElasticSearchList(List<Product> products) {
        // Map a list of Product to a list of ProductElasticSearch
        return products.stream()
                .map(ProductMapper::toElasticSearch)
                .collect(Collectors.toList());
    }
}
