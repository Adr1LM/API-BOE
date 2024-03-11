package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.Professor;
import com.paellasoft.CRUD.entity.Students;
import com.paellasoft.CRUD.repository.IProfessorRepository;
import com.paellasoft.CRUD.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Servicio {
    @Autowired
    private IStudentRepository IStudentrepo;
    @Autowired
    private IProfessorRepository IProfessorRepo;

    public Students saveStudent(Students student) {
        return IStudentrepo.save(student);
    }

    public List<Students> getStudents() {
        return IStudentrepo.findAll();
    }

    public Students editStudent(Integer id, Students student) {
        Optional<Students> studentsOptional = IStudentrepo.findById(id);
        if (studentsOptional.isPresent()) {
            Students currentStudent = studentsOptional.get();
            currentStudent.setName(student.getName());
            currentStudent.setLastname(student.getLastname());
            currentStudent.setEmail(student.getEmail());
            return IStudentrepo.save(currentStudent);
        }
        return null;
    }

    public Optional<Students> getStudentById(Integer id) {
        return IStudentrepo.findById(id);
    }

    public void deleteStudent(Integer id) {
        IStudentrepo.deleteById(id);
    }

    public Page<Students> getAllStudentsPaginados(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return IStudentrepo.findAll(pageable);
    }


    public Professor saveProfessor(Professor professor) {
        return IProfessorRepo.save(professor);
    }

    public List<Professor> getProfessors() {
        return IProfessorRepo.findAll();
    }

    public Professor editProfessor(Integer id, Professor professor) {
        Optional<Professor> professorOptional = IProfessorRepo.findById(id);
        if (professorOptional.isPresent()) {
            Professor currentProfessor = professorOptional.get();
            currentProfessor.setName(professor.getName());
            currentProfessor.setLastname(professor.getLastname());
            currentProfessor.setEmail(professor.getEmail());
            return IProfessorRepo.save(currentProfessor);
        }
        return null;
    }

    public Optional<Professor> getProfessorById(Integer id) {
        return IProfessorRepo.findById(id);
    }

    public void deleteProfessor(Integer id) {
        IProfessorRepo.deleteById(id);
    }

    public Page<Professor> getAllProfessorsPaginados(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return IProfessorRepo.findAll(pageable);
    }


}
