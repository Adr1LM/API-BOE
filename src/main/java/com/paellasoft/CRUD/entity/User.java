package com.paellasoft.CRUD.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private boolean sendNotification;

    @OneToMany(mappedBy = "user")
    private List<BoeUser> subscriptions;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public List<BoeUser> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<BoeUser> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void addSubscription(Boe boe) {
        if (subscriptions == null) {
            subscriptions = new ArrayList<>();
        }
        BoeUser boeUser = new BoeUser();
        boeUser.setUser(this);
        boeUser.setBoe(boe);
        subscriptions.add(boeUser);
    }

    public void removeSubscription(Boe boe) {
        if (subscriptions != null) {
            subscriptions.removeIf(boeUser -> boeUser.getBoe().equals(boe));
        }
    }

    public List<Boe> getSubscribedBoes() {
        if (subscriptions == null) {
            return Collections.emptyList();
        }
        return subscriptions.stream()
                .map(BoeUser::getBoe)
                .collect(Collectors.toList());
    }
}
