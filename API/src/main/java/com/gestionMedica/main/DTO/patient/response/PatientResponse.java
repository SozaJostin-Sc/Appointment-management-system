package com.gestionMedica.main.DTO.patient.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientResponse {
    private Long patientId;
    private Long userId;
    private String firstName;
    private String secondName;
    private String middleName;
    private String lastName;
    private LocalDate dateBirth;
    private char sex;
    private String phoneNumber;
}
