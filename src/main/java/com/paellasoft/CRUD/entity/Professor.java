package com.paellasoft.CRUD.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


    @Data
    @Entity
    @Table(name="professors")

    public class Professor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="id_professor")
        private Integer idProfessor;

        @Column(name="name")
        private String name;
        @Column(name="lastname")
        private String lastname;

        @Column(name="email")
        private String email;

        @Column(name="dni")
        private String dni;

        @JsonIgnore
        @OneToMany(cascade =
                CascadeType.ALL, fetch =
                FetchType.EAGER,
                orphanRemoval = true
                ,mappedBy = "professor")
        private List<Modulo> modulos;


        public void addModulo(Modulo modulo) {

            this.modulos.add(modulo);
        }

        public Integer getIdProfessor() {
            return idProfessor;
        }

        public void setIdProfessor(Integer idProfessor) {
            this.idProfessor = idProfessor;
        }

        public List<Modulo> getModulos() {
            return modulos;
        }

        public void setModulos(List<Modulo> modulos) {
            this.modulos = modulos;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


