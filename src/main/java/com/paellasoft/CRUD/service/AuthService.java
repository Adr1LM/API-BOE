package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Component
@Transactional
public class AuthService {


    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserService userService;


    public boolean validarSesion(Long userId, String sessionId) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(userId));
        return (optionalUser.isPresent() && Objects.equals(optionalUser.get().getId(), Long.parseLong(sessionId)));
    }
}
