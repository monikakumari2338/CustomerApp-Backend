package com.deepanshu.service;

import java.time.LocalDateTime;
import java.util.*;

import com.deepanshu.exception.ProductException;
import com.deepanshu.modal.User;
import com.deepanshu.repository.ProductRepository;
import com.deepanshu.repository.RatingRepository;
import com.deepanshu.repository.UserRepository;
import com.deepanshu.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepanshu.modal.Product;
import com.deepanshu.modal.Rating;

@Service
public class RatingServiceImplementation implements RatingServices {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;

    //update avg rating of product
    public double updateProductRating(List<Rating> ratingList){
        //calcualte avg rating
        double avgRating=0.0;
        int countTotalUserWhoRatedTheProduct=0;
        double totalRating=0.0;
        for(Rating rating:ratingList){
            totalRating+=rating.getGivenRating();
            countTotalUserWhoRatedTheProduct++;
        }
        return Math.round((totalRating/countTotalUserWhoRatedTheProduct)*100)/100;
    }

    //update avg rating of who rated5,4,3,2,1 individual rating
    public double updateIndividualRating(List<Rating> ratingList, double targetRatingToUpdate) {
        // Count how many users gave the target rating (1-5 stars)
        int countOfUsersWhoRatedTarget = 0;

        // Use a set to ensure only unique users are counted
        Set<Long> uniqueUsers = new HashSet<>();

        for (Rating rating : ratingList) {
            // Add user to the set of unique users
            uniqueUsers.add(rating.getUserId());

            // Count users who gave the specific target rating
            if (rating.getGivenRating() == targetRatingToUpdate) {
                countOfUsersWhoRatedTarget++;
            }
        }

        // Total number of unique users who rated the product
        int totalUsersWhoRated = uniqueUsers.size();

        if (totalUsersWhoRated == 0) {
            return 0.0; // Avoid division by zero
        }

        // Calculate the fraction (e.g., 2/4 for 5-star rating)
        return Math.round(((double) countOfUsersWhoRatedTarget / totalUsersWhoRated) * 100.0) / 100.0;
    }


    //update count total of each rating users
    public int countEachRatingUser(List<Rating> ratingList,double targetRatingToUpdate){
        // Use a set to ensure unique user IDs
        Set<Long> uniqueUsers = new HashSet<>();

        for (Rating rating : ratingList) {
            if (rating.getGivenRating() == targetRatingToUpdate) {
                uniqueUsers.add(rating.getUserId());  // Add only unique user IDs
            }
        }

        // The size of the set will give the count of unique users who gave the target rating
        return uniqueUsers.size();
    }

    @Override
    public Rating createRatingForProduct(RatingRequest ratingRequest) {
        //check the productId exist in the dataBase or not
        Product product=productRepository.findById(ratingRequest.getProductId()).orElseThrow(()->new RuntimeException("product not found in the database with givenId:"+ratingRequest.getProductId()));

        //check the userId exist in the DB
        userRepository.findById(ratingRequest.getUserId()).orElseThrow(()->new RuntimeException("user not exist with givenId:"+ratingRequest.getUserId()));

        //check if the user already rated the product
        List<Rating>isUserExist=ratingRepository.findByUserIdAndProductId(ratingRequest.getUserId(), ratingRequest.getProductId());

        Rating rating;
        if(!isUserExist.isEmpty()){
            rating=isUserExist.get(0);
            //update rating if user want to update rating
            Double updatedRating = ratingRequest.getGivenRatingByUser(); // Use Double here
            if (updatedRating != null) {
                rating.setGivenRating(updatedRating); // Update rating
            }
            //update comment if user want to update the comment
            rating.setComment(ratingRequest.getComment()!=null?ratingRequest.getComment():rating.getComment());
            rating.setCreatedAt(LocalDateTime.now());
            Rating createdRating = ratingRepository.save(rating);

            List<Rating> ratingList = product.getRatingList();
            ratingList.add(rating);
            //update product avg rating
            product.setProductRating(updateProductRating(ratingList));

            //update individual rating of who rated what rating
            product.setAverageRatingForOneStar(updateIndividualRating(ratingList,1.0));
            product.setAverageRatingForTwoStars(updateIndividualRating(ratingList,2.0));
            product.setAverageRatingForThreeStars(updateIndividualRating(ratingList,3.0));
            product.setAverageRatingForFourStars(updateIndividualRating(ratingList,4.0));
            product.setAverageRatingForFiveStars(updateIndividualRating(ratingList,5.0));
            //update count total of each rating users
            product.setCountUsersRatedProductOneStar(countEachRatingUser(ratingList,1.0));
            product.setCountUsersRatedProductTwoStars(countEachRatingUser(ratingList,2.0));
            product.setCountUsersRatedProductThreeStars(countEachRatingUser(ratingList,3.0));
            product.setCountUsersRatedProductFourStars(countEachRatingUser(ratingList,4.0));
            product.setCountUsersRatedProductFiveStars(countEachRatingUser(ratingList,5.0));
            product.setRatingList(ratingList);
            productRepository.save(product);
        }
        else {
            rating = new Rating();
            rating.setProductId(ratingRequest.getProductId());
            rating.setUserId(ratingRequest.getUserId());
            rating.setGivenRating(ratingRequest.getGivenRatingByUser());
            rating.setComment(ratingRequest.getComment());
            rating.setCreatedAt(LocalDateTime.now());


            Rating createdRating = ratingRepository.save(rating);

            List<Rating> ratingList = product.getRatingList();
            ratingList.add(rating);
            product.setProductRating(updateProductRating(ratingList));
            product.setRatingList(ratingList);
            //update product avg rating
            product.setProductRating(updateProductRating(ratingList));
            //update individual rating of who rated what rating
            product.setAverageRatingForOneStar(updateIndividualRating(ratingList,1.0));
            product.setAverageRatingForTwoStars(updateIndividualRating(ratingList,2.0));
            product.setAverageRatingForThreeStars(updateIndividualRating(ratingList,3.0));
            product.setAverageRatingForFourStars(updateIndividualRating(ratingList,4.0));
            product.setAverageRatingForFiveStars(updateIndividualRating(ratingList,5.0));
            //update count total of each rating users
            product.setCountUsersRatedProductOneStar(countEachRatingUser(ratingList,1.0));
            product.setCountUsersRatedProductTwoStars(countEachRatingUser(ratingList,2.0));
            product.setCountUsersRatedProductThreeStars(countEachRatingUser(ratingList,3.0));
            product.setCountUsersRatedProductFourStars(countEachRatingUser(ratingList,4.0));
            product.setCountUsersRatedProductFiveStars(countEachRatingUser(ratingList,5.0));
            productRepository.save(product);
            return createdRating;
        }
        return rating;
    }

    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
        return null;
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        return List.of();
    }
}