package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.dto.GetOrdersRequest;
import com.ftn.sbnz.model.dto.MessageResponse;
import com.ftn.sbnz.model.dto.OrderRequest;
import com.ftn.sbnz.model.models.Order;
import com.ftn.sbnz.model.models.OrderStatus;
import com.ftn.sbnz.model.models.User;
import com.ftn.sbnz.model.util.SecurityUtil;
import com.ftn.sbnz.service.repository.OrderRepository;
import com.ftn.sbnz.service.repository.UserRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImplementation implements  OrderService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    private final KieContainer kieContainer;

    OrderServiceImplementation( KieContainer kieContainer ) {
        this.kieContainer = kieContainer;
    }

    @Override
    public MessageResponse createOrder(OrderRequest orderRequest) {
        Optional<User> userOpt = userRepository.findByEmail(orderRequest.getEmail());
        if(userOpt.isEmpty()) {
            return new MessageResponse(false, "invalid user");
        }
        User user = userOpt.get();
        Order order = new Order(user,orderRequest.getRestaurant(),orderRequest.getOrderList(),
                orderRequest.getOrderPrice(),orderRequest.getIpAddress());

        KieSession kieSession = kieContainer.newKieSession("k-session");
        kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
        try {
            SecurityUtil util = new SecurityUtil();
            kieSession.setGlobal("securityUtil", util);

            kieSession.insert(user);
            kieSession.insert(order);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        userRepository.save(user);
        orderRepository.save(order);
        return new MessageResponse(true, "Successfully created");
    }

    @Override
    public MessageResponse cancelOrder(String email, UUID orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if(orderOpt.isEmpty()) {
            return new MessageResponse(false, "invalid order");
        }
        Order order = orderOpt.get();
        order.setStatus(OrderStatus.CANCELLED);

        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) {
            return new MessageResponse(false, "invalid user");
        }
        User user = userOpt.get();
        KieSession kieSession = kieContainer.newKieSession("k-session");
        kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
        try {
            SecurityUtil util = new SecurityUtil();
            kieSession.setGlobal("securityUtil", util);

            kieSession.insert(user);
            kieSession.insert(order);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        userRepository.save(user);
        orderRepository.save(order);
        return new MessageResponse(true, "Successfully cancelled");
    }

    @Override
    public List<GetOrdersRequest> getOrders(String email) {
        List<Order> orders = orderRepository.findByUserEmail(email);
        List<GetOrdersRequest> ordersRequests = new ArrayList<>();
        for (Order order : orders) {
            GetOrdersRequest getOrdersRequest = new GetOrdersRequest(order.getOrderId(),order.getRestaurant(),order.getOrderList(),order.getOrderPrice(), order.getCreationDate());
            ordersRequests.add(getOrdersRequest);
        }
        return ordersRequests;
    }
}
