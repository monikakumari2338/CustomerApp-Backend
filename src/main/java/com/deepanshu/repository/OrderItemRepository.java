package com.deepanshu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.deepanshu.modal.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.id IN :orderIds")
	List<OrderItem> findAllByOrderIds(@Param("orderIds") List<Long> orderIds);
}
