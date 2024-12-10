package com.deepanshu.controller;

import java.util.List;

import com.deepanshu.modal.CancellationReason;
import com.deepanshu.modal.OrderItem;
import com.deepanshu.modal.ReturnReason;
import com.deepanshu.request.CancelItemRequest;
import com.deepanshu.request.ExchangeItemRequest;
import com.deepanshu.request.ReturnItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deepanshu.exception.OrderException;
import com.deepanshu.modal.Order;
import com.deepanshu.response.ApiResponse;
import com.deepanshu.service.OrderService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "https://localhost:8081")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersHandler() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> ConfirmedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.confirmedOrder(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.shippedOrder(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliveredOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/{cancellationReason}/cancel")
    public ResponseEntity<Order> canceledOrderHandler(@PathVariable Long orderId, @RequestBody List<CancelItemRequest> cancelItemRequests,@PathVariable CancellationReason cancellationReason, @RequestParam(required = false) String comments, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.cancledOrder(orderId, cancelItemRequests,cancellationReason, comments);
        return new ResponseEntity<Order>(order, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        orderService.deleteOrder(orderId);
        ApiResponse res = new ApiResponse("Order Deleted Successfully", true);
        System.out.println("delete method working....");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/{returnReason}/return")
    public ResponseEntity<Order> returnProductHandler(@PathVariable Long orderId, @RequestBody List<ReturnItemRequest> returnItemRequests, @PathVariable ReturnReason returnReason, @RequestParam(required = false) String comments, @RequestParam(required = false) MultipartFile file, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.returnProduct(orderId, returnItemRequests, returnReason, comments, file);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/{returnReason}/exchange")
    public ResponseEntity<Order> exchangeProductHandler(@PathVariable Long orderId, @RequestBody List<ExchangeItemRequest> exchangeItemRequests, @PathVariable ReturnReason returnReason, @RequestParam(required = false) String comments, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.exchangeProduct(orderId, exchangeItemRequests, returnReason, comments);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }



    @PostMapping("/{orderId}/filter")
    public ResponseEntity<List<OrderItem>> filterOrderItems(@PathVariable Long orderId, @RequestBody List<Long> cancelledOrderItemsId) {
        try {
            List<OrderItem> filteredOrderItems = orderService.filteredOrderBasedOnOrderItems(orderId, cancelledOrderItemsId);
            return new ResponseEntity<>(filteredOrderItems, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //calculate refund amount based on cancel products in the order

}
