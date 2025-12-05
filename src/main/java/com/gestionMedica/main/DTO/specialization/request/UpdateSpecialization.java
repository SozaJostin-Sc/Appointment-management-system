package com.gestionMedica.main.DTO.specialization.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateSpecialization {
    @Size(max = 100, message = "Specialization name cannot be more than 100 characters")
    private String name;

    @Size(max = 250, message = "Specialization description cannot be more than 250 characters")
    private String description;

    public boolean dtoIsEmpty() {
        return (getName() == null || getName().trim().isEmpty()) && (getDescription() == null || getDescription().trim().isEmpty());
    }
}
