package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBoeRepository extends JpaRepository<Boe, Long> {
}
