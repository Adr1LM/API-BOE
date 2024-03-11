package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.Students;
import com.paellasoft.CRUD.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @Autowired
    private Servicio servicio;

    @GetMapping("/students")
    public List<Students> getAllStudents() {
        return servicio.getStudents();
    }

    @GetMapping("/studentsPage")
    public Page<Students> getAllStudentsPaginados(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        return servicio.getAllStudentsPaginados(page, size);
    }

    @PostMapping("/students/new")
    public Students addStudent(@RequestBody Students newStudent) {
        return servicio.saveStudent(newStudent);
    }

    @PutMapping("/students/edit/{id}")
    public Students editStudent(@PathVariable Integer id, @RequestBody Students student) {
        return servicio.editStudent(id, student);
    }

    @GetMapping("/students/{id}")
    public Optional<Students> getStudentById(@PathVariable Integer id) {
        return servicio.getStudentById(id);
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        servicio.deleteStudent(id);
    }
}
