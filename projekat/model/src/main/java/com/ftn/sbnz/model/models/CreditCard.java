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
    private Boolean isSuspicious;
    @Column
    private int numberOfAccounts;

    public CreditCard(String number){
        this.number = number;
        this.isSuspicious = false;
        this.numberOfAccounts = 1;
    }

    public CreditCard() {

    }

    public void addAccount(){
        this.numberOfAccounts++;
    }

    public Boolean getSuspicious() {
        return isSuspicious;
    }

    public void setSuspicious(Boolean suspicious) {
        isSuspicious = suspicious;
    }
}
