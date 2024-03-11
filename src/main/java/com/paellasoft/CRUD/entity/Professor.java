package com.paellasoft.CRUD.entity;

import jakarta.persistence.*;
import lombok.Data;


    @Data
    @Entity
    @Table(name="professors")

    public class Professor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="id_professor")
        private Integer idStudents;

        @Column(name="name")
        private String name;
        @Column(name="lastname")
        private String lastname;

        @Column(name="email")
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getIdStudents() {
            return idStudents;
        }

        public void setIdStudents(Integer idStudents) {
            this.idStudents = idStudents;
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


