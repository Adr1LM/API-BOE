package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.dto.UserDTO;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.AuthService;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;




    @PostMapping("/user/new")
    public void addUser(@RequestBody User newUser) {
         userService.registerUser(newUser);
    }



    @PostMapping("/user/suscribir")
    public ResponseEntity<String> suscribirUsuario(@RequestParam("userId") Long userId,
                                                   @RequestHeader("Session-Id") String sessionId) {
        System.out.println(sessionId); // Imprimir sessionId en la consola
        if (authService.validarSesion(userId, sessionId)){
        try {
                userService.suscribirUsuario(userId);
                return ResponseEntity.ok("El usuario se ha suscrito correctamente al Boletín Oficial del Estado.");
            }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al suscribir al usuario al Boletín Oficial del Estado.");
        }
        }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized access");
            }
        }




    @PostMapping("/user/baja")
    public ResponseEntity<String> darBaja(@RequestParam("userId") Long userId,
                                          @RequestHeader("Session-Id") String sessionId) {

        if (authService.validarSesion(userId, sessionId)){
        try {
             userService.bajaSubscripcion(userId);
                return ResponseEntity.ok("El usuario se ha dado de baja correctamente al Boletín Oficial del Estado.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al darse de baja el usuario al Boletín Oficial del Estado.");
        }
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized access");
        }
    }

    @DeleteMapping("/user/delete")
    public void deleteUser(@RequestParam Long userId){
        userService.deleteUserById(userId);
    }


    @DeleteMapping("/user/delete/all")
    public void deleteAllUsers(@RequestParam Long userId){
        userService.deleteAllUser();
    }

    @GetMapping("/user/dto/{id}")
    public ResponseEntity<UserDTO> obtenerUserPorId(@PathVariable Long id) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(id));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = UserDTO.fromEntity(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }









}
