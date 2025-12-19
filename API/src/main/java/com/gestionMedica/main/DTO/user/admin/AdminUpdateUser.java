package com.gestionMedica.main.DTO.user.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateUser {

    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    private String userName;

    @Email(message = "Debe ser un formato de correo electrónico válido.")
    @Size(max = 100, message = "El correo electrónico debe tener un máximo de 100 caracteres.")
    private String email;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    private Long rolId;
}
