package com.deepanshu.service;

import java.util.List;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.Review;
import com.deepanshu.modal.User;
import com.deepanshu.request.ReviewRequest;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;

    public List<Review> getAllReview(Long productId);


}
