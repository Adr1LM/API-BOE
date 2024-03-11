package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.Professor;
import com.paellasoft.CRUD.entity.Students;
import com.paellasoft.CRUD.repository.IProfessorRepository;
import com.paellasoft.CRUD.repository.IStudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServicioStudent {
    @Autowired
    private IStudentRepository IStudentrepo;

    @Autowired
    private IProfessorRepository IProfesRepo;


    public Students saveStudent(Students student){
        return IStudentrepo.save(student);
    }

    public List<Students> getStudents(){
        return IStudentrepo.findAll();
    }

    public Students editStudent(Integer id, Students student){
        Optional<Students> studentsOptional = IStudentrepo.findById(id);
        if (studentsOptional.isPresent()){
            Students currentStudent = studentsOptional.get();
            currentStudent.setName(student.getName());
            currentStudent.setLastname(student.getLastname());
            currentStudent.setEmail(student.getEmail());
            return  IStudentrepo.save(currentStudent);
        }
        return null;
    }


}
