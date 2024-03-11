package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository <Students, Integer> {

}
