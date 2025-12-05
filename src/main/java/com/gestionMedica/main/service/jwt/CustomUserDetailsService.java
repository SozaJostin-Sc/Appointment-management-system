// CustomUserDetailsService.java
package com.gestionMedica.main.service.jwt;

import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Buscando usuario con username: {}", username);

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con username: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        if (!user.getUserStatus()) {
            log.error("Usuario deshabilitado: {}", username);
            throw new UsernameNotFoundException("Usuario deshabilitado: " + username);
        }

        log.info("Usuario encontrado: {} con rol: {}", username,
                user.getRol() != null ? user.getRol().getRolName() : "Sin rol definido");

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getUserHashPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String roleName = user.getRol() != null ? user.getRol().getRolName() : "USER";
        String authority = "ROLE_" + roleName.toUpperCase();
        log.info("Asignando autoridad: {}", authority);
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }
}