package com.deepanshu.modal;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rating")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "productId")
	private Long productId;

	@Column(name = "userId")
	private Long userId;

	@Column(name = "givenRating")
	private double givenRating;

	@Column(name = "comment")
	private String comment;

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public double getGivenRating() {
		return givenRating;
	}

	public void setGivenRating(double givenRating) {
		this.givenRating = givenRating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Rating(Long id, Long productId, Long userId, double givenRating, String comment, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.productId = productId;
		this.userId = userId;
		this.givenRating = givenRating;
		this.comment = comment;
		this.createdAt = createdAt;
	}

	public Rating() {
		super();
		// TODO Auto-generated constructor stub
	}

}