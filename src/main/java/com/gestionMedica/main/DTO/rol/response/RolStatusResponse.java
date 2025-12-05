package com.gestionMedica.main.DTO.rol.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolStatusResponse {
    private String rolName;
    private String rolDescription;
    private Boolean rolStatus;
}
