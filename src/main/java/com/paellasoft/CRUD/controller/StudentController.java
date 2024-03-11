package com.paellasoft.CRUD.controller;


import com.paellasoft.CRUD.entity.Students;
import com.paellasoft.CRUD.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/api/v1")
public class StudentController {

    @Autowired
    private IStudentRepository IStudentRepository;
    @GetMapping ("/Students")
    public List<Students> getAllStudents(){
        return IStudentRepository.findAll();
    }

    //Resultados paginados
    @GetMapping ("/StudentsPage")
    public Page<Students> getAllStudentsPaginados(){
        final Pageable pageable = PageRequest.of(0,5);
        return IStudentRepository.findAll(pageable);
    }

    //Resultados paginados indicandole parametros (en Postman)
    @GetMapping ("/StudentsPageNombreAscendente")
    public Page<Students> getAllStudentsPaginadosNombre(Pageable pageable){
       // final Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.ASC));
        return IStudentRepository.findAll(pageable);
    }

    @GetMapping ("/StudentsPageVariable")
    public Page<Students> getAllStudentsVariablePage(@PageableDefault(page=0, size=20)Pageable pageable){
        return IStudentRepository.findAll(pageable);
    }



    @PostMapping("/students/new")
    public Students addStudent(@RequestBody Students newstudent){
        return IStudentRepository.save(newstudent);
    }

    @PatchMapping("/students/edit/{id}")
    public Students editStudent(@PathVariable Integer id, @RequestBody Students student){
        Optional<Students> studentsOptional = IStudentRepository.findById(id);
        if (studentsOptional.isPresent()){
            Students currentStudent = studentsOptional.get();
            currentStudent.setName(student.getName());
            currentStudent.setLastname(student.getLastname());
            currentStudent.setEmail(student.getEmail());
            return  IStudentRepository.save(currentStudent);
        }
        return null;
    }

    @GetMapping("/students/{id}")
    public Optional<Students> getStudentById(@PathVariable Integer id){
        return  IStudentRepository.findById(id);
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Integer id){
        IStudentRepository.deleteById(id);
    }

}
