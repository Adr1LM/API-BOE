package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            // Generar un ID de sesión único (en este caso, el ID de usuario)
            String sessionId = String.valueOf(user.getId());
            // Devolver el ID de sesión en el encabezado de respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add("Session-ID", sessionId);
            return new ResponseEntity<>("Login successful", headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@RequestHeader("Session-ID") String sessionId) {
        // Verificar la autenticidad del token
        Optional<User> optionalUser = userService.getUserById(Integer.parseInt(sessionId));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Verificar si el ID de sesión coincide con el ID de usuario
            if (user.getId() == Integer.parseInt(sessionId)) {
                // El token es válido, se permite acceder al recurso
                return new ResponseEntity<>("Resource accessed successfully", HttpStatus.OK);
            } else {
                // El token no coincide con el ID de usuario
                return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }
        } else {
            // No se encontró ningún usuario con el ID de sesión proporcionado
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
    }




}

