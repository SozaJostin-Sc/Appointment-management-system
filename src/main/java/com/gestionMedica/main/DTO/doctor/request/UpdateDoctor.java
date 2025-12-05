package com.gestionMedica.main.DTO.doctor.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateDoctor {

    @Size(max = 30, message = "First name size cannot be more than 30 characters")
    private String firstName;

    @Size(max = 30, message = "Second name size cannot be more than 30 characters")
    private String secondName;

    @Size(max = 30, message = "Middle name size cannot be more than 30 characters")
    private String middleName;

    @Size(max = 30, message = "Last name size cannot be more than 30 characters")
    private String lastName;

    @Size(min = 1, message = "At least one specialization must be provided.")
    private Set<Long> specializationIds;

}