package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.AuthService;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            // Generar un ID de sesión único (en este caso, el ID de usuario)
            String sessionId = String.valueOf(user.getId());
            // Devolver el ID de sesión en el encabezado de respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.add("Session-ID", sessionId);
            System.out.println(sessionId); // Imprimir sessionId en la consola
            return new ResponseEntity<>("Login successful", headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("userId") Long userId,
                                         @RequestHeader("Session-Id") String sessionId) {
        try {
            Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(userId));
            if (optionalUser.isPresent() && Objects.equals(optionalUser.get().getId(), Long.parseLong(sessionId))){
                System.out.println(sessionId);

                return ResponseEntity.ok("Logout successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized access");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during logout");
        }
    }

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@RequestHeader("Session-ID") String sessionId) {
        //metodo ejemplo para obtener recurso.

        // Verificar la autenticidad del token
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(Long.parseLong(sessionId)));
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

