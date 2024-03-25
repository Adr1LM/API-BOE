package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.BoeUser;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.mail.EmailSender;
import com.paellasoft.CRUD.repository.IBoeRepository;
import com.paellasoft.CRUD.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBoeRepository boeRepository;

    private PasswordEncoder passwordEncoder;


    private final EmailSender emailSender;

    public UserService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }


    public void registerUser(User user) {
        // Lógica para registrar al usuario
        userRepository.save(user);

        // Envío de correo electrónico de confirmación
        String to = user.getEmail();
        String subject = "Confirmación de registro";
        String text = "Hola " + user.getUsername() + ", tu registro ha sido exitoso.";
        emailSender.sendEmail(to, subject, text);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Transactional
    public void suscribirUsuario(Long userId) {
        // Obtener el usuario y el boletín oficial correspondientes
        Optional<User> optionalUser = userRepository.findById(userId);


        // Verificar si el usuario existe
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setSendNotification(true);


            // Envío de correo electrónico de notificacion
            String to = user.getEmail();
            String subject = "Confirmación de registro";
            String text = "Hola " + user.getUsername() + ", te has suscrito a Boe Newsletter.";
            emailSender.sendEmail(to, subject, text);

        } else {
            throw new RuntimeException("El usuario o el Boletín Oficial especificados no existen.");
        }
    }


    public void  deleteUserById(Long userId){
        userRepository.deleteById(userId);
    }

    public void deleteAllUser(){
        userRepository.deleteAll();
    }




}
