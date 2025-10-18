package com.ftn.sbnz.model.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    UUID id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private SuspicionLevel suspicionLevel;
    @Column(nullable = false)
    private boolean hasChangedAddress;
    @ManyToOne
    private Address address;
    @ManyToOne
    private CreditCard cardNumber;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private Instant creationDate;
    @Column(nullable = false)
    private boolean newAccount;
    @Column(nullable = false)
    private boolean isSuspended;
    @Column(nullable = false)
    private double totalSpentSinceLastSuspiciousAction;
    @ElementCollection
    private List<String> activePromoCodes;
    @Column
    private double moneySpent;
    @Column
    private Instant blockExpirationDate;
    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public double getTotalSpentSinceLastSuspiciousAction() {
        return totalSpentSinceLastSuspiciousAction;
    }

    public void setTotalSpentSinceLastSuspiciousAction(double totalSpentSinceLastSuspiciousAction) {
        this.totalSpentSinceLastSuspiciousAction = totalSpentSinceLastSuspiciousAction;
    }

    public User(String username, String email, String password, Address address, CreditCard cardNumber, String phone, Role role) {
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
        this.role = role;
        this.blockExpirationDate = null;
        this.moneySpent = 0;
        this.hasChangedAddress = false;
    }

    public User() {

    }

    public String getRealUsername() {
        return username;
    }
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isSuspended;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.isSuspended;
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

    public Instant getBlockExpirationDate() {
        return blockExpirationDate;
    }

    public void setBlockExpirationDate(Instant blockExpirationDate) {
        this.blockExpirationDate = blockExpirationDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+this.role.toString()));
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CreditCard getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(CreditCard cardNumber) {
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

    public boolean isHasChangedAddress() {
        return hasChangedAddress;
    }

    public void setHasChangedAddress(boolean hasChangedAddress) {
        this.hasChangedAddress = hasChangedAddress;
    }
}
