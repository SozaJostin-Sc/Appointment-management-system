package com.gestionMedica.main.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expiry", nullable = false)
    private LocalDateTime expiry;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Método auxiliar para verificar si el token ha expirado
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiry);
    }

    public boolean isValid() {
        return !isExpired() && !isUsed;
    }
}