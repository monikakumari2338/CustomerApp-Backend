package com.deepanshu.service;

import com.deepanshu.modal.Order;
import org.springframework.stereotype.Service;

import com.deepanshu.modal.OrderItem;
import com.deepanshu.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

    private OrderItemRepository orderItemRepository;

    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {

        return orderItemRepository.save(orderItem);
    }


}
