package com.paellasoft.CRUD.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean sendNotification;
    private List<BoeUserDTO> subscriptions;

    // Constructor, Getters y Setters

    public UserDTO() {}

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

    public List<BoeUserDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<BoeUserDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
