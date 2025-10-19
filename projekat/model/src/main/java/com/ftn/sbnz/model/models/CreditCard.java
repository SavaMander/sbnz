package com.ftn.sbnz.model.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class CreditCard {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column
    private String number;

    @Column
    private Boolean suspicious; // FIX 2: Renamed field from isSuspicious

    @Column
    private int numberOfAccounts;

    public CreditCard(String number){
        this.number = number;
        this.suspicious = false; // Updated to match field name
        this.numberOfAccounts = 1;
    }

    public CreditCard() {
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

    // --- FIX 3: ADD GETTER/SETTER FOR numberOfAccounts ---
    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }

    public void setNumberOfAccounts(int numberOfAccounts) {
        this.numberOfAccounts = numberOfAccounts;
    }

    // --- Other existing methods ---
    public void addAccount(){
        this.numberOfAccounts++;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}