package com.ftn.sbnz.model.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID orderId;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @Column(nullable = false)
    private double orderPrice;
    @Column(nullable = false)
    private OrderStatus status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ip_address_id")
    private IPAddress ipAddress;
    @Column(nullable = false)
    private String restaurant;
    @Column(nullable = false)
    private String orderList;
    @Column(nullable = false)
    private Instant creationDate;
    @Column
    private Instant cancellationDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public Order(User user, double orderPrice) {
        this.user = user;
        this.orderPrice = orderPrice;
        this.status = OrderStatus.CREATED;
        this.creationDate = Instant.now();
    }

    public Order() {

    }
    public Order(User user, String restaurant, String orderList, double orderPrice, String ipAddress) {
        this.user = user;
        this.restaurant = restaurant;
        this.orderList = orderList;
        this.orderPrice = orderPrice;
        this.ipAddress =  new IPAddress(ipAddress);
        this.status = OrderStatus.CREATED;
        this.creationDate = Instant.now();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public User getCustomerAccount() {
        return user;
    }

    public void setCustomerAccount(User user) {
        this.user = user;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public IPAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IPAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Instant cancellationDate) {
        this.cancellationDate = cancellationDate;
    }
}
