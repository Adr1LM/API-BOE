package com.paellasoft.CRUD.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="modulos")
public class Modulo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="codigo")
    private Integer codModulo;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_professor", referencedColumnName = "id_Professor")
    private Professor professor;

    @ManyToMany(mappedBy = "modulos")
    @JsonIgnore
    private List<Student> students;


    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Integer getCodModulo() {
        return codModulo;
    }

    public void setCodModulo(Integer codModulo) {
        this.codModulo = codModulo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
