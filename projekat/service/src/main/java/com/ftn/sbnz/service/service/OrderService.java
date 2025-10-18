package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.GetOrdersRequest;
import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.OrderRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    public MessageResponse createOrder(OrderRequest orderRequest);
    public MessageResponse cancelOrder(String email, UUID orderId);
    public List<GetOrdersRequest> getOrders(String email);
}
