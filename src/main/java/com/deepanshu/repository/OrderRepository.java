package com.deepanshu.repository;

import java.util.List;

import com.deepanshu.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("SELECT o FROM Order o WHERE o.user.id = :userId")
	public List<Order> getUsersOrders(@Param("userId") Long userId);

	List<Order> findByUser(User user);

	public List<Order> findTop5ByUserOrderByCreatedAtDesc(User user);
}