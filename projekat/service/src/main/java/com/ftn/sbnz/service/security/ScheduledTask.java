package com.ftn.sbnz.service.security;

import com.ftn.sbnz.model.models.Order;
import com.ftn.sbnz.model.models.OrderStatus;
import com.ftn.sbnz.model.util.SecurityUtil;
import com.ftn.sbnz.service.repository.OrderRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

@EnableScheduling
@Configuration
public class ScheduledTask {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KieContainer kieContainer;
    @Scheduled(fixedRate = 15*1000, initialDelay = 5000)
    public void finishOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order: orders) {
            if(order.getStatus().equals(OrderStatus.CREATED) && order.getCreationDate().plusSeconds(15).isBefore(Instant.now())){
                order.setStatus(OrderStatus.DELIVERED);
                KieSession kieSession = kieContainer.newKieSession("k-session");
                kieSession.addEventListener(new org.kie.api.event.rule.DebugAgendaEventListener());
                try {
                    SecurityUtil util = new SecurityUtil();
                    kieSession.setGlobal("securityUtil", util);
                    kieSession.insert(order);
                    kieSession.insert(order.getUser());
                    System.out.println("Firing all rules...");
                    kieSession.getAgenda().getAgendaGroup("promotion_level_1").setFocus();
                    int rulesFired = kieSession.fireAllRules();
                    System.out.println("Finished. Rules fired: " + rulesFired);
                } finally {
                    kieSession.dispose();
                }
            }

        }
        orderRepository.saveAll(orders);
    }
}
