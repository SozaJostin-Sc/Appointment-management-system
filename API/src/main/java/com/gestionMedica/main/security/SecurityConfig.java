package com.gestionMedica.main.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración principal para Spring Security.
 * @Configuration: Marca la clase como fuente de definiciones de beans.
 * @EnableWebSecurity: Habilita el soporte de seguridad en Spring.
 * @RequiredArgsConstructor: Genera un constructor para inicializar los campos 'final' (como jwtAuthenticationFilter).
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Inyecta el filtro JWT personalizado que interceptará las peticiones para validar el token
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configura la cadena de filtros de seguridad HTTP principal.
     * Este es el núcleo de la configuración de seguridad.
     * * @param http El objeto HttpSecurity para configurar la seguridad.
     * @return El SecurityFilterChain construido.
     * @throws Exception Si ocurre un error de configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF (Cross-Site Request Forgery).
                // Es común deshabilitarlo en APIs REST que usan tokens (como JWT) para autenticación.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configuración de la gestión de sesiones.
                // SessionCreationPolicy.STATELESS: No se crearán ni usarán sesiones HTTP.
                // Es esencial para arquitecturas sin estado como las basadas en JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configuración de las reglas de autorización de peticiones HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso público (sin autenticación) a los endpoints de autenticación (login/registro)
                        .requestMatchers("/api/auth/**", "api/medical/user/admin/**").permitAll()
                        // Permitir acceso público a endpoints de roles
                        .requestMatchers("/api/medical/roles/**").hasRole("PATIENT")
                        // Permitir acceso público a endpoints de usuario (ej. registro)
                        .requestMatchers("/api/medical/user/**").permitAll()

                        // Restringir el acceso: Solo los usuarios con el ROL "ADMIN" pueden acceder a /api/medical/admin/**
                        //.requestMatchers("/api/medical/admin/**").hasRole("ADMIN")

                        // Regla Catch-All: Para cualquier otra petición (anyRequest)
                        // se requiere que el usuario esté autenticado (authenticated()).
                        .anyRequest().authenticated()
                )

                // 4. Añadir el filtro JWT personalizado.
                // Este filtro se ejecutará ANTES (addFilterBefore) del filtro estándar de autenticación
                // por usuario/contraseña (UsernamePasswordAuthenticationFilter) para validar el token JWT
                // en cada solicitud.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construir y devolver la cadena de filtros de seguridad configurada.
        return http.build();
    }


}