package com.paellasoft.CRUD.controller;


import com.paellasoft.CRUD.entity.Students;
import com.paellasoft.CRUD.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
