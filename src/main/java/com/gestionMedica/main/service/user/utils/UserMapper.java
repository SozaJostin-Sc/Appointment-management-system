package com.gestionMedica.main.service.user.utils;

import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.DTO.user.response.UserStatusResponse;
import com.gestionMedica.main.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /*
    * Convierte una entidad User en un DTO de respuesta
    * @param user La entidad user a mapear
    * @return UserResponse, el dto. Limpio para enviar al cliente
    * */
    public UserResponse toDto(User user){
        if(user == null)
            return null;

        return UserResponse.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .rolName(user.getRol() != null ? user.getRol().getRolName() : "Sin rol definido")
                .status(user.getUserStatus())
                .dateCreation(user.getDateCreation())
                .build();
    }

    public UserStatusResponse toStatusDto(User user){
        if(user == null)
            return null;

        return UserStatusResponse.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .status(user.getUserStatus())
                .dateCreation(user.getDateCreation())
                .build();
    }


    /*
    * Convierte una lista de entidades a una lista de DTO
    * */
    public List<UserResponse> toDtoList(List<User> users){
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
