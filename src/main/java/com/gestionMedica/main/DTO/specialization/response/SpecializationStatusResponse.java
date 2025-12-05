package com.gestionMedica.main.DTO.specialization.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecializationStatusResponse {
    private Long id;
    private String name;
    private boolean status;
}
