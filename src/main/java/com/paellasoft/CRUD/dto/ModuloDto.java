package com.paellasoft.CRUD.dto;

import java.io.Serializable;

public class ModuloDto implements Serializable {

    private String name;

    public ModuloDto(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
