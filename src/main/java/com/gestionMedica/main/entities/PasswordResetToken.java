package com.gestionMedica.main.entities;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public PasswordResetToken(User user, String hashedToken, LocalDateTime expiryDate) {
    }


    // Método auxiliar para verificar si el token ha expirado
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiry);
    }

    public boolean isValid() {
        return !isExpired() && !isUsed;
    }
}