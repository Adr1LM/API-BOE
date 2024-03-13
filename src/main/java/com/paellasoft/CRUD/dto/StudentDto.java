package com.paellasoft.CRUD.dto;

import java.io.Serializable;

public class StudentDto implements Serializable {

    private Integer idStudents;
    private String name;
    private String lastname;

    public StudentDto(Integer idStudents, String name, String lastname) {
        this.idStudents = idStudents;
        this.name = name;
        this.lastname = lastname;
    }

    public Integer getIdStudents() {
        return idStudents;
    }

    public void setIdStudents(Integer idStudents) {
        this.idStudents = idStudents;
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
