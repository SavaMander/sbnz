package com.ftn.sbnz.model.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerAccount {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private SuspicionLevel suspicionLevel;
    private String address;
    private String cardNumber;
    private String phone;
    private Instant creationDate;
    private boolean newAccount;
    private boolean isSuspended;
    private double totalSpentSinceLastSuspiciousAction;
    private List<String> activePromoCodes;

    public CustomerAccount(String username, String email, String password, String address, String cardNumber, String phone) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.suspicionLevel = SuspicionLevel.VALID;
        this.address = address;
        this.cardNumber = cardNumber;
        this.phone = phone;
        this.creationDate = Instant.now();
        this.newAccount = true;
        this.totalSpentSinceLastSuspiciousAction = 0.0;
        this.isSuspended = false;
        this.activePromoCodes = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SuspicionLevel getSuspicionLevel() {
        return suspicionLevel;
    }

    public void setSuspicionLevel(SuspicionLevel suspicionLevel) {
        this.suspicionLevel = suspicionLevel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isNewAccount() {
        return newAccount;
    }

    public void setNewAccount(boolean newAccount) {
        this.newAccount = newAccount;
    }

    public List<String> getActivePromoCodes() {
        return activePromoCodes;
    }

    public void setActivePromoCodes(List<String> activePromoCodes) {
        this.activePromoCodes = activePromoCodes;
    }
}
