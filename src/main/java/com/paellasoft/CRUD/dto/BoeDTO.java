package com.paellasoft.CRUD.dto;

import java.util.List;

public class BoeDTO {
    private Long id;
    private String contenidoOriginal;
    private String contenidoResumido;
    private String fechaBoe;
    private List<BoeUserDTO> subscriptions;

    // Constructor
    public BoeDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenidoOriginal() {
        return contenidoOriginal;
    }

    public void setContenidoOriginal(String contenidoOriginal) {
        this.contenidoOriginal = contenidoOriginal;
    }

    public String getContenidoResumido() {
        return contenidoResumido;
    }

    public void setContenidoResumido(String contenidoResumido) {
        this.contenidoResumido = contenidoResumido;
    }

    public String getFechaBoe() {
        return fechaBoe;
    }

    public void setFechaBoe(String fechaBoe) {
        this.fechaBoe = fechaBoe;
    }

    public List<BoeUserDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<BoeUserDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
