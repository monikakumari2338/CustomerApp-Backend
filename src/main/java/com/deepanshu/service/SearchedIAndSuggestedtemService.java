package com.deepanshu.service;

import java.util.List;

import com.deepanshu.modal.Product;

public interface SearchedIAndSuggestedtemService {

	String saveSearchedItems(String param, Long userId);

	List<Product> getSuggestedProducts(Long userId);
}