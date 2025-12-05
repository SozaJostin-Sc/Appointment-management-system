package com.gestionMedica.main.DTO.specialization.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SpecializationResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateCreation;
    private boolean status;
}
