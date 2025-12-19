package com.gestionMedica.main.service.auth;

import com.gestionMedica.main.DTO.user.request.LoginRequest;
import com.gestionMedica.main.DTO.user.request.RegisterUserDTO;
import com.gestionMedica.main.DTO.user.response.LoginResponse;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.jwt.JwtService;
import com.gestionMedica.main.service.patient.PatientService;
import com.gestionMedica.main.service.user.UserService;
import com.gestionMedica.main.service.user.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final PatientService patientService;
    private final UserMapper userMapper;

    public LoginResponse login(LoginRequest loginRequest) {

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
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // Generar el token JWT
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            // Determinar si es admin
            boolean isAdmin = user.getRol() != null &&
                    "admin".equalsIgnoreCase(user.getRol().getRolName());

            log.info("Login exitoso para usuario: {}", loginRequest.getUserName());

            return new LoginResponse(
                    jwt,
                    refreshToken,
                    "Bearer",
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserEmail(),
                    user.getRol().getRolName(),
                    isAdmin
            );

    }

    public LoginResponse refreshToken(String refreshToken){
        String userName = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = userService.loadUserByUsername(user.getUserName());

        if(jwtService.isTokenValid(refreshToken, userDetails)){
            String newAccessToken = jwtService.generateToken(userDetails);

            return  new LoginResponse(
                    newAccessToken,
                    refreshToken,
                    "Bearer",
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserEmail(),
                    user.getRol().getRolName(),
                    "admin".equalsIgnoreCase(user.getRol().getRolName())
            );
        }else{
            throw new RuntimeException("Invalid refresh token");
        }
    }

    public UserResponse register(RegisterUserDTO dto){
        User user = userService.create(dto);
        log.info("Iniciando creación de usuario");
        patientService.createDefaultPatient(user);
        return userMapper.toDto(user);
    }







}