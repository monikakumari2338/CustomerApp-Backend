package com.deepanshu.service;

import java.util.List;

import com.deepanshu.exception.OrderException;
import com.deepanshu.modal.*;
import com.deepanshu.request.CancelItemRequest;
import com.deepanshu.request.ExchangeItemRequest;
import com.deepanshu.request.ReturnItemRequest;
import org.springframework.web.multipart.MultipartFile;

public interface OrderService {

    public Order createOrder(User user, Address shippingAdress, StorePickup storePickup);

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public Order placedOrder(Long orderId) throws OrderException;

    public Order confirmedOrder(Long orderId) throws OrderException;

    public Order shippedOrder(Long orderId) throws OrderException;

    public Order deliveredOrder(Long orderId) throws OrderException;

    public Order cancledOrder(Long orderId, List<CancelItemRequest> cancelItemRequests, CancellationReason cancellationReason, String comments) throws OrderException;

    public List<Order> getAllOrders();

    public void deleteOrder(Long orderId) throws OrderException;

    Order returnProduct(Long orderId, List<ReturnItemRequest> returnItemRequests, ReturnReason returnReason, String comments, MultipartFile file) throws OrderException;

    Order exchangeProduct(Long orderId, List<ExchangeItemRequest> exchangeItemRequests, ReturnReason returnReason, String comments) throws OrderException;

//    Order cancelOrderItem(Long orderId, List<Long> orderItemIds, CancellationReason cancellationReason, String comments) throws OrderException;

    Order createOrderForSubscriptionDelivery(Subscription subscription);

    //filter the order by orderItem ids
    List<OrderItem>filteredOrderBasedOnOrderItems(Long orderId,List<Long>cancelledOrderItemsId);
}
