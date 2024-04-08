package com.paellasoft.CRUD.dto;

import com.paellasoft.CRUD.entity.Boe;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import lombok.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoeDTO  {

    private Long id;

    private String tituloBoe;

    private String fechaBoe;

    //Metodo para pasar Boe a DTO
    public static BoeDTO fromEntity(Boe boe) {
        BoeDTO boeDTO = new BoeDTO();
        boeDTO.setId(boe.getId());
        boeDTO.setTituloBoe(boe.getTituloBoe());
        boeDTO.setFechaBoe(boe.getFechaBoe());
        return boeDTO;
    }
    //Metodo para pasar DTO a Boe
    public static Boe toEntity(BoeDTO boeDTO) {
        Boe boe = new Boe();
        boe.setId(boeDTO.getId());
        boe.setTituloBoe(boeDTO.getTituloBoe());
        boe.setFechaBoe(boeDTO.getFechaBoe());
        return boe;
    }
}
