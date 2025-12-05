package com.gestionMedica.main.service.auth;

import com.gestionMedica.main.DTO.user.LoginRequest;
import com.gestionMedica.main.DTO.user.response.LoginResponse;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            log.info("Intentando login con username: {}", loginRequest.getUserName());

            // Se delega la autentificación a spring security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obtener el usuario de la base de datos por username
            User user = userRepository.findByUserName(loginRequest.getUserName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar el token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            // Determinar si es admin
            boolean isAdmin = user.getRol() != null &&
                    "admin".equalsIgnoreCase(user.getRol().getRolName());

            log.info("Login exitoso para usuario: {}", loginRequest.getUserName());

            return new LoginResponse(
                    jwt,
                    "Bearer",
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserEmail(),
                    user.getRol().getRolName(),
                    isAdmin
            );
        } catch (Exception e) {
            log.error("Error en autenticación para {}: {}", loginRequest.getUserName(), e.getMessage());
            throw new RuntimeException("Error en autenticación: " + e.getMessage());
        }
    }
}