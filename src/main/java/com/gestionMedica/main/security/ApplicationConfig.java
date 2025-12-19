package com.gestionMedica.main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
    /**
     * Define un Bean para el codificador de contraseñas.
     * Usamos BCrypt, que es un algoritmo de hashing fuerte y recomendado.
     * @return El bean PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Define un Bean para el AuthenticationManager.
     * El AuthenticationManager es necesario para el proceso de inicio de sesión.
     * Se obtiene de la configuración de autenticación proporcionada por Spring.
     * @param config La configuración de autenticación.
     * @return El AuthenticationManager.
     * @throws Exception Si ocurre un error al obtener el manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
