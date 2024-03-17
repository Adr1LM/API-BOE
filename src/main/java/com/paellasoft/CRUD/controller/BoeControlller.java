package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.service.BoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BoeControlller {

    @Autowired
    private BoeService boeService;

    @PostMapping("/boe/resumen")
    public String obtenerResumenBoeDelDia() {
        return boeService.obtenerBoeDelDia();
    }





}
