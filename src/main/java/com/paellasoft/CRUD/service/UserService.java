package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.mail.EmailSender;
import com.paellasoft.CRUD.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    private final EmailSender emailSender;

    public UserService(EmailSender emailSender) {
        this.emailSender = emailSender;
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

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }



}
