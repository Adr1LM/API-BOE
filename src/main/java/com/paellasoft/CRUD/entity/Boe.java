package com.paellasoft.CRUD.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="boe")
public class Boe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "contenido_original")
    private String contenidoOriginal;
    @Column(name = "contenido_resumido")
    private String contenidoResumido;
    @Column(name="fecha_boe")
    private LocalDate fechaBoe;

}
