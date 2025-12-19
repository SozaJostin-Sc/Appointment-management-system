package com.gestionMedica.main.DTO.patient.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientStatusResponse {
    private Long patientId;
    private Long userId;
    private String firstName;
    private String lastName;
    private boolean status;
}
