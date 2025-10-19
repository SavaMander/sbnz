package com.ftn.sbnz.model.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Address {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id; // Problem 1 was here

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean suspicious; // Problem 2 was here (field renamed)

    @Column(nullable = false)
    private int numberOfAccounts;

    public Address(String address){
        this.address = address;
        this.suspicious = false; // Updated to match field name
        this.numberOfAccounts = 1;
    }

    public Address() {
    }

    // --- FIX 1: ADD GETTER/SETTER FOR ID ---
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // --- Method to check suspicion ---
    public Boolean getSuspicious() { // FIX 2: Getter renamed to "is..."
        return suspicious;
    }

    public void setSuspicious(Boolean suspicious) { // FIX 2: Setter updated
        this.suspicious = suspicious;
    }

    // --- Other existing methods ---
    public void addAccount(){
        this.numberOfAccounts++;
    }

    public void removeAccount(){
        this.numberOfAccounts--;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }

    public void setNumberOfAccounts(int numberOfAccounts) {
        this.numberOfAccounts = numberOfAccounts;
    }
}