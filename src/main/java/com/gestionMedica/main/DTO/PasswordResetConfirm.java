package com.gestionMedica.main.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class PasswordResetConfirm {

    @NotBlank(message = "El token es obligatorio")
    private String token;

    @NotBlank(message = "La nueva contrasena es obligatoria")
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    private String newPassword;

    @NotBlank(message = "La confirmacion de la contrasena debe ser obligatoria")
    private String confirmPassword;

}
