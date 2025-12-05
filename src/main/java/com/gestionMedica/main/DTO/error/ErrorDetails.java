package com.gestionMedica.main.DTO.error;

import lombok.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ErrorDetails {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
}
