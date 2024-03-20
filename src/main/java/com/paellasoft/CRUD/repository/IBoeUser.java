package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.entity.BoeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBoeUser extends JpaRepository<BoeUser, Long> {


    List<BoeUser> findByBoe(Boe ultimoBoe);
}
