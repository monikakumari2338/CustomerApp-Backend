package com.deepanshu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Cart;
import com.deepanshu.modal.CartItem;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	@Query("SELECT ci From CartItem ci Where ci.cart=:cart And ci.sku=:sku And ci.userId=:userId")
	public CartItem isCartItemExist(@Param("cart") Cart cart, @Param("sku") String size, @Param("userId") Long userId);

	List<CartItem> findByUserId(Long userId);
}
