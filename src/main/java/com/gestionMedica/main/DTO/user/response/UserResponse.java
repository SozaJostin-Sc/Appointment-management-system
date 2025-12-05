package com.gestionMedica.main.DTO.user.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String userName;
    private String userEmail;
    private String rolName;
    private Boolean status;
    private LocalDateTime dateCreation;
}
