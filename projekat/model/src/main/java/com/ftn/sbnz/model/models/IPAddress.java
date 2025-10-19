package com.ftn.sbnz.model.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class IPAddress {
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
    private Boolean suspicious; // FIX 2: Renamed field from isSuspicious

    public IPAddress(String address){
        this.address = address;
        this.suspicious = false; // Updated to match field name
    }

    public IPAddress() {
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}