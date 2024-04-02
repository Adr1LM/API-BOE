package com.paellasoft.CRUD.dto;

public class BoeUserDTO {
    private Long id;
    private UserDTO user;
    private BoeDTO boe;

    // Constructor
    public BoeUserDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public BoeDTO getBoe() {
        return boe;
    }

    public void setBoe(BoeDTO boe) {
        this.boe = boe;
    }
}
