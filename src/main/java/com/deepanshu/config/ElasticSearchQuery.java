package com.deepanshu.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.deepanshu.modal.ProductElasticSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "products";

    private String extractTopLevelCategoryFromQuery(String query) {
        // Define top-level categories

        String l1 = "women";
        String l2 = "men";

        if (query.toLowerCase().contains(l1)) {
            return l1;
        } else if (query.toLowerCase().contains(l2)) {
            return l2;
        }
        return null; // Return null if no category is found
    }


    public List<ProductElasticSearch> fuzzySearch(String query) throws IOException {
        // Define the fields you want to search in
        String[] searchFields = {
                "title",
                "brand",
                "description",
                "topLevelCategory",
                "secondLevelCategory",
                "thirdLevelCategory",
                "productCode",
                "fabric",
                "wearType",
                "materialCare",
                "seller"
        };

        // Extract the top-level category from the query if present
        String topLevelCategory = extractTopLevelCategoryFromQuery(query);

        // Print the indexName values
        System.out.println("Index Name: " + indexName+" "+topLevelCategory);

        // Perform the initial fuzzy search
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)
                .size(10000)
                .query(q -> q
                        .bool(b -> {
                            // Add a filter for topLevelCategory if defined
                            if (topLevelCategory != null) {
                                b.filter(f -> f
                                        .term(t -> t
                                                .field("topLevelCategory")
                                                .value(topLevelCategory)
                                        )
                                );
                            }

                            // Add the fuzzy search criteria
                            b.must(m -> m
                                    .bool(bb -> {
                                        // Loop through each field and add a fuzzy query for it
                                        for (String field : searchFields) {
                                            bb.should(sh -> sh
                                                    .fuzzy(f -> f
                                                            .field(field)
                                                            .value(query)
                                                            .prefixLength(3)
                                                    )
                                            );
                                        }
                                        return bb;
                                    })
                            );

                            return b;
                        })
                )
        );

        // Execute the search
        SearchResponse<ProductElasticSearch> searchResponse =
                elasticsearchClient.search(searchRequest, ProductElasticSearch.class);

        // Extract hits and map them to a list of ProductElasticSearch
        List<Hit<ProductElasticSearch>> hits = searchResponse.hits().hits();
        List<ProductElasticSearch> products = new ArrayList<>();

        for (Hit<ProductElasticSearch> hit : hits) {
            products.add(hit.source());
        }

        // If no results are found, perform a suffix search
        if (products.isEmpty()) {
            // Update suffix length to 4
            int suffixLength = Math.min(4, query.length());
            String suffixQuery = "*" + query.substring(Math.max(query.length() - suffixLength, 0)) + "*";

            SearchRequest suffixSearchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(q -> q
                            .bool(b -> {
                                // Add a filter for topLevelCategory if defined
                                if (topLevelCategory != null) {
                                    b.filter(f -> f
                                            .term(t -> t
                                                    .field("topLevelCategory")
                                                    .value(topLevelCategory)
                                            )
                                    );
                                }

                                // Add the suffix search criteria
                                b.must(m -> m
                                        .bool(bb -> {
                                            for (String field : searchFields) {
                                                bb.should(sh -> sh
                                                        .wildcard(w -> w
                                                                .field(field)
                                                                .value(suffixQuery)
                                                        )
                                                );
                                            }
                                            return bb;
                                        })
                                );
                                return b;
                            })
                    )
            );

            SearchResponse<ProductElasticSearch> suffixResponse =
                    elasticsearchClient.search(suffixSearchRequest, ProductElasticSearch.class);

            // Extract hits and map them to a list of ProductElasticSearch
            hits = suffixResponse.hits().hits();
            for (Hit<ProductElasticSearch> hit : hits) {
                products.add(hit.source());
            }
        }

        return products;
    }


    public String createOrUpdateDocument(ProductElasticSearch product) throws IOException {
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(product.getId())
                .document(product)
        );
        if (response.result().name().equals("Created")) {
            return "Document has been successfully created.";
        } else if (response.result().name().equals("Updated")) {
            return "Document has been successfully updated.";
        }
        return "Error while performing the operation.";
    }

    public ProductElasticSearch getDocumentById(String productId) throws IOException {
        GetResponse<ProductElasticSearch> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(productId),
                ProductElasticSearch.class
        );

        if (response.found()) {
            ProductElasticSearch product = response.source();
            System.out.println("Product name: " + product.getName());
            return product;
        } else {
            System.out.println("Product not found");
            return null;
        }
    }

    public String deleteDocumentById(String productId) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(productId));
        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return "Product with id " + deleteResponse.id() + " has been deleted.";
        }
        System.out.println("Product not found");
        return "Product with id " + deleteResponse.id() + " does not exist.";
    }

    public List<ProductElasticSearch> searchAllDocuments() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse<ProductElasticSearch> searchResponse =
                elasticsearchClient.search(searchRequest, ProductElasticSearch.class);

        List<Hit<ProductElasticSearch>> hits = searchResponse.hits().hits();
        List<ProductElasticSearch> products = new ArrayList<>();

        for (Hit<ProductElasticSearch> hit : hits) {
            System.out.println(hit.source());  // Log the product for debugging
            products.add(hit.source());
        }

        return products;
    }


}
