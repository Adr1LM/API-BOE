package com.paellasoft.CRUD.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
@Table(name="boe")
public class Boe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String contenidoOriginal;
    private String contenidoResumido;
    private Date fechaBoe;

}
