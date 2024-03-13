package com.paellasoft.CRUD.dto;

import java.io.Serializable;

public class ProfessorDto implements Serializable {

    private Integer idProfessor;
    private String name;
    private String lastname;

    public ProfessorDto(Integer idProfessor, String name, String lastname) {
        this.idProfessor = idProfessor;
        this.name = name;
        this.lastname = lastname;
    }

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
