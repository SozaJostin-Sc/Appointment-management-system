package com.gestionMedica.main.DTO.rol.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RolResponse {
    private Long rolId;
    private String rolName;
    private String rolDescription;
    private Boolean rolStatus;
    private LocalDateTime dateCreation;
}

