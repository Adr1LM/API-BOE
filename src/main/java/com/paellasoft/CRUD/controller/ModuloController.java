package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.Modulo;
import com.paellasoft.CRUD.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ModuloController {


    @Autowired
    private Servicio servicio;

    @GetMapping("/modulos")
    public List<Modulo> getAllModulos() {
        return servicio.getModulos();
    }

    @PostMapping("/modulos/new")
    public Modulo addModulo(@RequestBody Modulo newModulo) {
        return servicio.saveModulo(newModulo);
    }

    @PutMapping("/modulos/edit/{id}")
    public Modulo editModulo(@PathVariable Integer id, @RequestBody Modulo modulo) {
        return servicio.editModulo(id, modulo);
    }

    @PutMapping("/modulos/{idMod}/edit/profe/{id}")
    public Modulo editModuloProfe(@PathVariable Integer id, @PathVariable Integer idMod) {
        return servicio.editModuloProfe(id, idMod);
    }

    @DeleteMapping("/modulos/{id}")
    public void deleteModulo(@PathVariable Integer codigo) {
        servicio.deleteModulo(codigo);
    }




}
