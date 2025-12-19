package com.gestionMedica.main.DTO.doctor.response;

import com.gestionMedica.main.entities.Specialization;
import lombok.Builder;
import java.util.Set;

@Builder
public class DoctorResponse {
    private Long userId;
    private Long doctorId;
    private String firstName;
    private String secondName;
    private String middleName;
    private String lastName;
    private Boolean status;
    private Set<Specialization> specializations;
}
