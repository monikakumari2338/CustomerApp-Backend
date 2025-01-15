package com.deepanshu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Wishlist;
import com.deepanshu.modal.WishlistItem;
import com.deepanshu.modal.Product;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

	@Query("SELECT ci From WishlistItem ci Where ci.wishlist=:wishlist And ci.sku=:sku And ci.userId=:userId")
	public WishlistItem isWishlistItemExist(@Param("wishlist") Wishlist wishlist, 
			@Param("sku") String sku, @Param("userId") Long userId);

	@Query("SELECT wli FROM WishlistItem wli WHERE wli.userId = :userId")
	List<WishlistItem> findAllByUserId(@Param("userId") Long userId);
}