package com.paellasoft.CRUD.repository;

import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.entity.BoeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBoeUser extends JpaRepository<BoeUser, Long> {

    @Query("SELECT bu.boe FROM BoeUser bu WHERE bu.user.id = :userId")
    List<Boe> findBoesByUserId(Long userId);

    @Query("SELECT b FROM Boe b WHERE b NOT IN (SELECT bu.boe FROM BoeUser bu WHERE bu.user.id = :userId)")
    List<Boe> findNotReceivedBoesByUserId(Long userId);



    List<BoeUser> findByBoe(Boe ultimoBoe);
}
