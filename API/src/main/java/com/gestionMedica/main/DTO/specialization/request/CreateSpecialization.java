package com.gestionMedica.main.DTO.specialization.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSpecialization {
    @NotBlank(message = "Specialization name is required")
    @Size(max = 100, message = "Specialization name cannot be more than 100 characters")
    private String name;

    @Size(max = 250, message = "Specialization description cannot be more than 250 characters")
    private String description;
}
