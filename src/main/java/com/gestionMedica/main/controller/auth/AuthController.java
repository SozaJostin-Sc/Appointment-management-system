package com.gestionMedica.main.controller.auth;

import com.gestionMedica.main.DTO.resetPassword.PasswordResetConfirm;
import com.gestionMedica.main.DTO.resetPassword.PasswordResetRequest;
import com.gestionMedica.main.DTO.user.request.LoginRequest;
import com.gestionMedica.main.DTO.user.request.RegisterUserDTO;
import com.gestionMedica.main.DTO.user.request.RefreshTokenRequest;
import com.gestionMedica.main.DTO.user.response.*;
import com.gestionMedica.main.service.auth.AuthService;
import com.gestionMedica.main.service.passwordReset.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService resetService;

    /// LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /// REFRESH TOKEN
    @PostMapping("/refres-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken){
        return ResponseEntity.ok(authService.refreshToken(refreshToken.getRefreshToken()));
    }

    /// SIGN IN
    @PostMapping("/register")
    public ResponseEntity<UserResponse> create(@RequestBody RegisterUserDTO dto){
        return new ResponseEntity<>(authService.register(dto), HttpStatus.CREATED);
    }

    /// RECOVER-PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetRequest request) throws MessagingException {
        resetService.createPasswordResetToken(request.getEmail());
        return ResponseEntity.ok("Si el correo esta registrado, recibiras un enlace de recuperacion");
    }

    /// RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetConfirm request){
        resetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password updated");
    }

}