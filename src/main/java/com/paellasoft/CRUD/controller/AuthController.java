package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.AuthService;
import com.paellasoft.CRUD.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Auth Controller", description = "Endpoints para operaciones relacionadas autenticación")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "Log in de usuario. Devuelve el token de sesión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("SessionID", token);
            return new ResponseEntity<>("Login successful", headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Log out de usuario. Invalida el token de sesión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Parameter(description = "Session token required for logout") @RequestHeader("Session-Id") String token) {
        if (authService.validarSesion(token)) {
            authService.logout(token);
            return ResponseEntity.ok("Logout successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
    }

    @Operation(summary = "Acceso a recurso protegido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource accessed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@Parameter(description = "Session ID needed to access resources") @RequestHeader("Session-ID") String sessionId) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserById(Long.parseLong(sessionId)));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getId() == Integer.parseInt(sessionId)) {
                return new ResponseEntity<>("Resource accessed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
    }

}
