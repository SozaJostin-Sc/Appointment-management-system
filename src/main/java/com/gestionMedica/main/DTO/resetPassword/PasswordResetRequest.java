package com.gestionMedica.main.DTO.resetPassword;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
public class PasswordResetRequest {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato de email no es valido")
    private String email;
}

