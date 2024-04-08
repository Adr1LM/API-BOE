package com.paellasoft.CRUD.dto;

import com.paellasoft.CRUD.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;


    private String username;


    private boolean sendNotification;

    // Método para convertir de User a UserDTO
    public static UserDTO fromEntity(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setSendNotification(user.isSendNotification());
        return userDTO;
    }

    // Método para convertir de UserDTO a User
    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setSendNotification(userDTO.isSendNotification());
        return user;
    }
}
