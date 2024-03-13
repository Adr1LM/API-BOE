package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.Modulo;
import com.paellasoft.CRUD.entity.Professor;
import com.paellasoft.CRUD.entity.Student;
import com.paellasoft.CRUD.repository.IModuloRepository;
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

    @Autowired
    private IModuloRepository IModuloRepo;


    @Autowired
    private IModuloRepository IAsignacionRepo;


    public Student saveStudent(Student student) {return IStudentrepo.save(student);
    }

    public List<Student> getStudents() {
        return IStudentrepo.findAll();
    }

    public Student editStudent(Integer id, Student student) {
        Optional<Student> studentsOptional = IStudentrepo.findById(id);
        if (studentsOptional.isPresent()) {
            Student currentStudent = studentsOptional.get();
            currentStudent.setName(student.getName());
            currentStudent.setLastname(student.getLastname());
            currentStudent.setEmail(student.getEmail());
            return IStudentrepo.save(currentStudent);
        }
        return null;
    }

    public Optional<Student> getStudentById(Integer id) {
        return IStudentrepo.findById(id);
    }

    public void deleteStudent(Integer id) {
        IStudentrepo.deleteById(id);
    }

    public Page<Student> getAllStudentsPaginados(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return IStudentrepo.findAll(pageable);
    }

//--------------------------------- PARTE PROFESSOR ---------------------------------------------
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




    //--------------------------------- PARTE MODULO ---------------------------------------------


    public Modulo saveModulo(Modulo modulo) {
        return IModuloRepo.save(modulo);
    }

    public List<Modulo> getModulos() {
        return IModuloRepo.findAll();
    }

    public Modulo editModulo(Integer id, Modulo modulo) {
        Optional<Modulo> moduloOptional = IModuloRepo.findById(id);
        if (moduloOptional.isPresent()) {
            Modulo currentModulo = moduloOptional.get();
            currentModulo.setName(modulo.getName());
            currentModulo.setCodModulo(modulo.getCodModulo());
            return IModuloRepo.save(currentModulo);
        }
        return null;
    }


    public Modulo editModuloProfe(Integer idModulo, Integer idProfesor) {
        // Buscar el módulo por su ID
        Optional<Modulo> moduloOptional = IModuloRepo.findById(idModulo);
        if (moduloOptional.isPresent()) {
            // Buscar el profesor por su ID
            Optional<Professor> professorOptional = IProfessorRepo.findById(idProfesor);
            if (professorOptional.isPresent()) {
                Modulo currentModulo = moduloOptional.get();
                Professor professor = professorOptional.get();
                // Asignar el profesor al módulo
                currentModulo.setProfessor(professor);
                // Guardar el módulo actualizado en la base de datos
                return IModuloRepo.save(currentModulo);
            }
        }
        return null; // Retornar null si no se encuentra el módulo o el profesor con los IDs proporcionados
    }




    public void deleteModulo(Integer codigo) {
        IModuloRepo.deleteById(codigo);
    }




    //-------------------- ASIGNACIONES MODULOS ----------------------------------------





    public Student addModuloToStudent(Integer studentId, Integer moduloId) {
        // Obtener el estudiante y el módulo por sus respectivos IDs
        Student student = IStudentrepo.findById(studentId).orElse(null);
        Modulo modulo = IModuloRepo.findById(moduloId).orElse(null);

        // Verificar si el estudiante y el módulo existen
        if (student != null && modulo != null) {
            // Asignar el módulo al estudiante
            student.addModulo(modulo);
            // Guardar el estudiante actualizado en la base de datos
            IStudentrepo.save(student);
        }

        return student;
    }


    public Professor addModuloToProfessor(Integer professorId, Integer moduloId) {


        // Obtener el estudiante y el módulo por sus respectivos IDs
        Professor professor = IProfessorRepo.findById(professorId).orElse(null);
        Modulo modulo = IModuloRepo.findById(moduloId).orElse(null);

        // Verificar si el estudiante y el módulo existen
        if (professor != null && modulo != null) {
            // Asignar el módulo al estudiante
            professor.addModulo(modulo);
            // Guardar el estudiante actualizado en la base de datos
            IProfessorRepo.save(professor);
        }

        return professor;
    }
}
