package com.gestionMedica.main.DTO.doctor.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class CreateDoctor {

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 30, message = "First name size cannot be more than 30 characters")
    private String firstName;

    @NotBlank(message = "Second name cannot be empty")
    @Size(max = 30, message = "Second name size cannot be more than 30 characters")
    private String secondName;

    @NotBlank(message = "Middle name cannot be empty")
    @Size(max = 30, message = "Middle name size cannot be more than 30 characters")
    private String middleName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 30, message = "Last name size cannot be more than 30 characters")
    private String lastName;

    @NotNull(message = "At least one specialization must be provided.")
    @Size(min = 1, message = "At least one specialization must be provided.")
    private Set<Long> specializationIds;

}
