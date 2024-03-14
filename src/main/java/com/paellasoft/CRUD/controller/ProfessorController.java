package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.dto.ProfessorDto;
import com.paellasoft.CRUD.entity.Professor;
import com.paellasoft.CRUD.entity.Student;
import com.paellasoft.CRUD.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ProfessorController {

    @Autowired
    private Servicio servicio;

    @GetMapping("/professors")
    public List<ProfessorDto> getAllProfessors() {
        return servicio.getProfessors();
    }

    @GetMapping("/professorsPage")
    public Page<Professor> getAllProfessorsPaginados(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "5") int size) {
        return servicio.getAllProfessorsPaginados(page, size);
    }

    @PostMapping("/professors/new")
    public Professor addProfessor(@RequestBody Professor newProfessor) {
        return servicio.saveProfessor(newProfessor);
    }

    @PutMapping("/professors/edit/{id}")
    public Professor editProfessor(@PathVariable Integer id, @RequestBody Professor professor) {
        return servicio.editProfessor(id, professor);
    }

    @GetMapping("/professors/{id}")
    public Optional<Professor> getProfessorById(@PathVariable Integer id) {
        return servicio.getProfessorById(id);
    }

    @DeleteMapping("/professors-delete/{id}")
    public void deleteProfessor(@PathVariable Integer id) {
        servicio.deleteProfessor(id);
    }

    @PutMapping("/professor/{professorId}/addModulo/{moduloId}")
    public Professor addModuloToProfessor(@PathVariable Integer professorId, @PathVariable Integer moduloId) {
        return servicio.addModuloToProfessor(professorId, moduloId);
    }
}
