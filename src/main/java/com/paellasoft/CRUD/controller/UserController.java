package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.Student;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.Servicio;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/user/new")
    public void addUser(@RequestBody User newUser) {
         userService.registerUser(newUser);
    }



}
