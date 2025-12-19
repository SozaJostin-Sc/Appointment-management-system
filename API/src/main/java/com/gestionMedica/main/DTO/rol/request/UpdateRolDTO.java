package com.gestionMedica.main.DTO.rol.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRolDTO {
    @Size(min = 0, max = 100, message = "Rol name cannot be more than 100 and less than 0")
    private String rolName;

    @Size(min = 0, max = 200, message = "Rol description cannot be more than 200 and less than 0")
    private String rolDescription;

    public boolean isEmpty(){
        return (getRolName() == null || getRolName().trim().isEmpty()) && (getRolDescription() == null || getRolDescription().trim().isEmpty());
    }
}
