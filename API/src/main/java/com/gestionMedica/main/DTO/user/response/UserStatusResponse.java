package com.gestionMedica.main.DTO.user.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserStatusResponse {
    private Long userId;
    private String username;
    private Boolean status;
    private LocalDateTime dateCreation;
}
