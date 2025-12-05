/**package com.gestionMedica.main.service;

import com.gestionMedica.main.DTO.PasswordResetRequest;
import com.gestionMedica.main.entitys.PasswordResetToken;
import com.gestionMedica.main.entitys.User;
import com.gestionMedica.main.repository.PasswordResetTokenRepository;
import com.gestionMedica.main.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_EXPIRY_HOURS = 24;
    private static final int TOKEN_LENGTH = 32;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:3000")
    private String frontendUrl;

    public boolean requestPasswordReset(PasswordResetRequest request){
        try{
            Optional<User> userOpt = userRepository.findActiveUserByEmail(request.getEmail());

            if(userOpt.isEmpty()){
                logger.info("Intento de recuperacion para email no registrado: {}", request.getEmail());

                return true;
            }

            User user = userOpt.get();

            //Invalidar tokens previos no utilizados
            tokenRepository.invalidateAllUserTokens(user);


            //Generar token unico
            String rawToken = generateRandomToken();
            String hashedToken = hashToken(rawToken);

            //Calcular fecha de expiracion
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);

            //Crear y guardar token
            PasswordResetToken resetToken = new PasswordResetToken(user, hashedToken, expiryDate);
            tokenRepository.save(resetToken);










        }
    }



}
*/