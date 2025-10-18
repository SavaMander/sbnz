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
    private UUID id;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Boolean isSuspicious;
    @Column(nullable = false)
    private int numberOfAccounts;

    public Address(String address){
        this.address = address;
        this.isSuspicious = false;
        this.numberOfAccounts = 1;
    }

    public Address() {

    }
    public void addAccount(){
        this.numberOfAccounts++;
    }
    public void removeAccount(){this.numberOfAccounts--;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getSuspicious() {
        return isSuspicious;
    }

    public void setSuspicious(Boolean suspicious) {
        isSuspicious = suspicious;
    }
}
