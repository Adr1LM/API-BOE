package com.paellasoft.CRUD.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "boe_user")
public class BoeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "boe_id")
    private Boe boe;



    //private Long customNoRecibidos;


    private static  List<User> suscritos;




    public BoeUser (){
        this.suscritos=null;

    }

    public List<User> getSuscritos() {
        return suscritos;
    }

    public void setSuscritos(List<User> suscritos) {
        this.suscritos = suscritos;
    }


// Constructor, getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boe getBoe() {
        return boe;
    }

    public void setBoe(Boe boe) {
        this.boe = boe;
    }
}
