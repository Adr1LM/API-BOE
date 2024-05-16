package com.paellasoft.CRUD.service;

import com.paellasoft.CRUD.entity.User;
import com.paellasoft.CRUD.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    @Autowired
    private IUserRepository userRepository;

    // Mapa para almacenar tokens activos y sus correspondientes IDs de usuario
    private Map<String, Long> activeSessions = new HashMap<>();

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().matches(password)) {
            // Generar token incluyendo la ID del usuario
            String token = UUID.randomUUID().toString() + ":" + user.getId();
            // Almacenar token en el mapa de sesiones activas
            activeSessions.put(token, user.getId());
            return token;
        } else {
            return null;
        }
    }

    public boolean validarSesion(String token) {
        if (token != null && !token.isEmpty()) {
            // Extraer el ID del usuario del token
            String[] parts = token.split(":");
            if (parts.length > 1) {
                try {
                    Long userId = Long.parseLong(parts[1]);
                    // Verificar si el token está en el mapa de sesiones activas y corresponde al mismo ID de usuario
                    return activeSessions.containsKey(token) && activeSessions.get(token).equals(userId);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public void logout(String token) {

        activeSessions.remove(token);
    }
}
