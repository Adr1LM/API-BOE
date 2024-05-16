package com.paellasoft.CRUD.controller;

import com.paellasoft.CRUD.dto.UserDTO;
import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.service.AuthService;
import com.paellasoft.CRUD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "UserController", description = "Controlador para la gestión de usuarios")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente")
    @PostMapping("/user/new")
    public void addUser(@RequestBody User newUser) {
        userService.registerUser(newUser);
    }

    @Operation(summary = "Suscribir usuario al BOE", description = "Suscribe a un usuario al Boletín Oficial del Estado")
    @ApiResponse(responseCode = "200", description = "Usuario suscrito correctamente")
    @PostMapping("/user/suscribir")
    public ResponseEntity<String> suscribirUsuario(@Parameter(description = "Token de sesión del usuario") @RequestHeader("Session-Id") String sessionId) {
        if (authService.validarSesion(sessionId)){
            try {
                Long userId = Long.parseLong(sessionId.split(":")[1]);
                userService.suscribirUsuario(userId);
                return ResponseEntity.ok("El usuario se ha suscrito correctamente al Boletín Oficial del Estado.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al suscribir al usuario al Boletín Oficial del Estado: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized access");
        }
    }

    @Operation(summary = "Dar de baja al usuario", description = "Da de baja a un usuario del Boletín Oficial del Estado")
    @ApiResponse(responseCode = "200", description = "Usuario dado de baja correctamente")
    @PostMapping("/user/baja")
    public ResponseEntity<String> darBaja(@Parameter(description = "Token de sesión del usuario") @RequestHeader("Session-Id") String sessionId) {
        if (authService.validarSesion(sessionId)){
            try {
                Long userId = Long.parseLong(sessionId.split(":")[1]);
                userService.bajaSubscripcion(userId);
                return ResponseEntity.ok("El usuario se ha dado de baja correctamente al Boletín Oficial del Estado.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al darse de baja el usuario al Boletín Oficial del Estado: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized access");
        }
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente")
    @DeleteMapping("/user/delete")
    public void deleteUser(@Parameter(description = "ID del usuario a eliminar") @RequestParam Long userId){
        userService.deleteUserById(userId);
    }

    @Operation(summary = "Eliminar todos los usuarios", description = "Elimina todos los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Todos los usuarios han sido eliminados")
    @DeleteMapping("/user/delete/all")
    public void deleteAllUsers(){
        userService.deleteAllUser();
    }

    @Operation(summary = "Obtener datos de usuario por ID", description = "Devuelve los datos de un usuario específico por su ID")
    @ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))})
    @GetMapping("/user/dto/{id}")
    public ResponseEntity<UserDTO> obtenerUserPorId(@Parameter(description = "ID del usuario a recuperar") @PathVariable Long id) {
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
