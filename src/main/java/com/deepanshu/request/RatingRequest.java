package com.deepanshu.request;

import com.deepanshu.modal.Product;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
	private double givenRatingByUser;
	private String comment;
	private Long userId;
	private Long productId;

	public double getGivenRatingByUser() {
		return givenRatingByUser;
	}

	public void setGivenRatingByUser(double givenRatingByUser) {
		this.givenRatingByUser = givenRatingByUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public RatingRequest(double givenRatingByUser, String comment, Long userId, Long productId) {
		super();
		this.givenRatingByUser = givenRatingByUser;
		this.comment = comment;
		this.userId = userId;
		this.productId = productId;
	}

	public RatingRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
