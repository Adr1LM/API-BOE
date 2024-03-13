package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository <Student, Integer> {

}
