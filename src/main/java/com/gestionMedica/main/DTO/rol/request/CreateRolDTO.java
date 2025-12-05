package com.gestionMedica.main.DTO.rol.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRolDTO {
    @NotBlank(message = "Rol name cannot be empty")
    @Size(min = 0, max = 100, message = "Rol name cannot be more than 100 and less than 0")
    private String rolName;

    @Size(min = 0, max = 200, message = "Rol description cannot be more than 200 and less than 0")
    private String rolDescription;
}
