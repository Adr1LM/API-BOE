package com.paellasoft.CRUD.repository.custom;

import com.paellasoft.CRUD.entity.Boe;
import com.paellasoft.CRUD.repository.IBoeUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRepository implements ICustomRepository {



    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Boe> customNoRecibidos(Long userId) {




        System.out.println("Test de uso del metodo custom");
        String hql = "SELECT b FROM Boe b " +
                "WHERE b NOT IN " +
                "(SELECT bu.boe FROM BoeUser bu " +
                "WHERE bu.user.id = :userId)";
        TypedQuery<Boe> query = entityManager.createQuery(hql, Boe.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
