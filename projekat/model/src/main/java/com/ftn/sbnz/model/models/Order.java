package com.ftn.sbnz.model.models;

import java.util.UUID;

public class Order {

    private UUID orderId;
    private CustomerAccount customerAccount;
    private double orderPrice;
    private OrderStatus status;


    public Order(CustomerAccount customerAccount, double orderPrice) {
        this.customerAccount = customerAccount;
        this.orderPrice = orderPrice;
        this.status = OrderStatus.CREATED;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
