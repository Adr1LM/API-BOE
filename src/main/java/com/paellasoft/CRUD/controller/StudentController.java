package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.dto.StudentDto;
import com.paellasoft.CRUD.entity.Student;
import com.paellasoft.CRUD.service.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @Autowired
    private Servicio servicio;

    @GetMapping("/students")
    public List<StudentDto> getAllStudents() {
        return servicio.getStudents();
    }

    @GetMapping("/studentsPage")
    public Page<Student> getAllStudentsPaginados(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {
        return servicio.getAllStudentsPaginados(page, size);
    }

    @PostMapping("/students/new")
    public Student addStudent(@RequestBody Student newStudent) {
        return servicio.saveStudent(newStudent);
    }

    @PutMapping("/students/edit/{id}")
    public Student editStudent(@PathVariable Integer id, @RequestBody Student student) {
        return servicio.editStudent(id, student);
    }

    @GetMapping("/students/{id}")
    public Optional<Student> getStudentById(@PathVariable Integer id) {
        return servicio.getStudentById(id);
    }

    @DeleteMapping("/students-delete/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        servicio.deleteStudent(id);
    }


    @PutMapping("/students/{studentId}/addModulo/{moduloId}")
    public Student addModuloToStudent(@PathVariable Integer studentId, @PathVariable Integer moduloId) {
        return servicio.addModuloToStudent(studentId, moduloId);
    }

}
