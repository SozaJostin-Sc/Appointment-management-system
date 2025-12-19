package com.gestionMedica.main.service.passwordReset;

import com.gestionMedica.main.entities.PasswordResetToken;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.exceptions.passwordToken.InvalidTokenException;
import com.gestionMedica.main.exceptions.passwordToken.TokenExpiredException;
import com.gestionMedica.main.exceptions.user.UserNotFoundException;
import com.gestionMedica.main.repository.PasswordResetTokenRepository;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetService{
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) throws MessagingException {
        User user = userRepository.findByUserEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found")
        );

        //Generate token
        String rawToken = UUID.randomUUID().toString();

        /// Hash token
        String hashedToken = passwordEncoder.encode(rawToken);

        PasswordResetToken myToken = PasswordResetToken.builder()
                .tokenHash(hashedToken)
                .user(user)
                .expiry(LocalDateTime.now().plusMinutes(30))
                .isUsed(false)
                .build();

        passwordResetTokenRepository.save(myToken);

        emailService.sendPasswordResetEmail(user.getUserEmail(), rawToken);

    }




    public void resetPassword(String rawToken, String newPassword){
        List<PasswordResetToken> activeTokens = passwordResetTokenRepository.findByIsUsedFalse();

        PasswordResetToken validToken = activeTokens.stream()
                .filter(t -> passwordEncoder.matches(rawToken, t.getTokenHash()))
                .findFirst()
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if(validToken.isExpired()){
            throw new TokenExpiredException("Token is expired");
        }

        User user = validToken.getUser();
        user.setUserHashPassword(passwordEncoder.encode(newPassword));

        validToken.setIsUsed(true);
    }

}
