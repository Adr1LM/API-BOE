package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfessorRepository extends JpaRepository<Professor, Integer> {

}
