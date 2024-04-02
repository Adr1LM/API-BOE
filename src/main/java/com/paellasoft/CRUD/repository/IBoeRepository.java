package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Boe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBoeRepository extends JpaRepository<Boe, Long> {

    Boe findTopByOrderByFechaBoeDesc();

    Boe findByFechaBoe(String fechaBoe);

    Boe findTopByOrderByIdDesc();
}
