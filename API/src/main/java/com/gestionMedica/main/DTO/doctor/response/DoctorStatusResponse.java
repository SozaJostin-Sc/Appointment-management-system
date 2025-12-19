package com.gestionMedica.main.DTO.doctor.response;

import lombok.Builder;

@Builder
public class DoctorStatusResponse {
    private Long userId;
    private Long doctorId;
    private String firstName;
    private String lastName;
    private Boolean status;
}
