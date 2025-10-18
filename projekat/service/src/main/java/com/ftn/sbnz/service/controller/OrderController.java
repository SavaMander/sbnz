package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.dto.GetOrdersRequest;
import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.OrderRequest;
import com.ftn.sbnz.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/orders") // For frontend development, adjust as needed for production
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createOrder( @RequestBody OrderRequest orderRequest) {
        MessageResponse response = orderService.createOrder(orderRequest);
        if (response.getSuccessful()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<MessageResponse> cancelOrder(@PathVariable UUID orderId, @RequestParam String email) {
        MessageResponse response = orderService.cancelOrder(email, orderId);
        if (response.getSuccessful()) {
            return ResponseEntity.ok(response);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping()
    public ResponseEntity<List<GetOrdersRequest>> getMyOrders(@RequestParam String email) {
        List<GetOrdersRequest> orders = orderService.getOrders(email);
        return ResponseEntity.ok(orders);
    }
}