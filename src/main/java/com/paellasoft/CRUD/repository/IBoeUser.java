package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.BoeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBoeUser extends JpaRepository<BoeUser, Long> {
}
