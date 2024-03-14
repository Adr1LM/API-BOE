package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserService userService;



    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        if (userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }
}
