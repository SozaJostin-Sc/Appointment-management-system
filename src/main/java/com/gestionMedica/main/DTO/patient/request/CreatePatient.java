package com.gestionMedica.main.DTO.patient.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePatient {

    @NotNull(message = "The ID of the associated user is required.")
    private Long userId;

    @NotBlank(message = "The first name cannot be empty.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    private String firstName;

    @Size(max = 50, message = "Second name must not exceed 50 characters.")
    private String secondName;

    @Size(max = 50, message = "Middle name must not exceed 50 characters.")
    private String middleName;

    @NotBlank(message = "The last name cannot be empty.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    private String lastName;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateBirth;

    @NotNull(message = "Sex is required.")
    @Pattern(regexp = "[MmFfOo]", message = "Sex must be 'M', 'F' or 'O'.")
    private String sex;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 7, max = 20, message = "Phone number must be between 7 and 20 characters.")
    @Pattern(
            regexp = "^\\+?[0-9\\-\\s\\(\\)]*$",
            message = "Phone number format is invalid. Use digits, spaces, hyphens, parentheses, and an optional plus sign."
    )
    private String phoneNumber;

}