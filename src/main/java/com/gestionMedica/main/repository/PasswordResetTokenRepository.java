package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    List<PasswordResetToken> findByUserAndIsUsedFalse(User user);
    List<PasswordResetToken> findByIsUsedFalse();

    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.userEmail = :email AND t.isUsed = false")
    List<PasswordResetToken> findValidTokensByUserEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.isUsed = true WHERE t.user = :user AND t.isUsed = false")
    void invalidateAllUserTokens(@Param("user") User user);
}
