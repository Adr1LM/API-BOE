package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.Student;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.Servicio;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;



    @PostMapping("/user/new")
    public void addUser(@RequestBody User newUser) {
         userService.registerUser(newUser);
    }

    @PostMapping("/user/suscribir")
    public ResponseEntity<String> suscribirUsuario(@RequestParam("userId") Long userId) {
        try {
            userService.suscribirUsuario(userId);
            return ResponseEntity.ok("El usuario se ha suscrito correctamente al Boletín Oficial del Estado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al suscribir al usuario al Boletín Oficial del Estado.");
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





}
