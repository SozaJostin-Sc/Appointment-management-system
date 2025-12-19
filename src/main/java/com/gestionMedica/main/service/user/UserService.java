package com.gestionMedica.main.service.user;

import com.gestionMedica.main.DTO.user.request.RegisterUserDTO;
import com.gestionMedica.main.DTO.user.request.UpdateUser;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.DTO.user.response.UserStatusResponse;
import com.gestionMedica.main.entities.*;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.exceptions.user.*;
import com.gestionMedica.main.repository.RolRepository;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.user.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final UserMapper userMapper;

    // Get by ID
    public UserResponse getById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = findUserById(id);
        log.info("User found: {} (ID: {})", user.getUserName(), id);
        return userMapper.toDto(user);
    }

    public User create(RegisterUserDTO dto) {
        log.info("Creating new user with username: {}", dto.getUserName());

        // Check if email already exists
        if (userRepository.existsByUserEmail(dto.getEmail())) {
            log.warn("Email already exists for username {}: {}", dto.getUserName(), dto.getEmail());
            throw new UserEmailAlreadyExistException("Email: " + dto.getEmail() + " already exists");
        }

        // Check if username already exists
        if (userRepository.existsByUserName(dto.getUserName())) {
            log.warn("Username already exists: {}", dto.getUserName());
            throw new UsernameAlreadyExistException("Username: " + dto.getUserName() + " already exists");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        log.debug("Password encrypted successfully for user: {}", dto.getUserName());

        Rol defaultRol = rolRepository.findByRolNameIgnoreCase("patient");
        if (defaultRol == null) {
            log.error("Default role 'patient' not found in database");
            throw new RuntimeException("Default role 'patient' not found");
        }
        log.debug("Assigned default role: {}", defaultRol.getRolName());

        User newUser = User.builder()
                .userName(dto.getUserName())
                .userEmail(dto.getEmail())
                .userHashPassword(encryptedPassword)
                .userStatus(true)
                .dateCreation(LocalDateTime.now())
                .rol(defaultRol)
                .build();

        log.info("Creating user object for: {}", dto.getUserName());
        userRepository.save(newUser);
        log.info("User created successfully: {} (ID: {})", newUser.getUserName(), newUser.getUserId());

        return newUser;
    }

    public UserResponse update(UpdateUser dto, Long id) {
        log.info("Updating user with ID: {}", id);

        User existingUser = findUserById(id);
        log.info("Found user to update: {} (ID: {})", existingUser.getUserName(), id);

        // Validate email uniqueness if changed
        if (dto.getEmail() != null &&
                !dto.getEmail().equals(existingUser.getUserEmail()) &&
                userRepository.existsByUserEmail(dto.getEmail())) {
            log.warn("Email already in use: {}", dto.getEmail());
            throw new UserEmailAlreadyExistException("Email " + dto.getEmail() + " is already in use");
        }

        // Validate username uniqueness if changed
        if (dto.getUserName() != null &&
                !dto.getUserName().equals(existingUser.getUserName()) &&
                userRepository.existsByUserName(dto.getUserName())) {
            log.warn("Username already in use: {}", dto.getUserName());
            throw new UsernameAlreadyExistException("Username " + dto.getUserName() + " is already in use");
        }

        // Update fields if provided
        boolean changesMade = false;

        if (dto.getUserName() != null) {
            log.debug("Updating username from '{}' to '{}'", existingUser.getUserName(), dto.getUserName());
            existingUser.setUserName(dto.getUserName());
            changesMade = true;
        }

        if (dto.getEmail() != null) {
            log.debug("Updating email from '{}' to '{}'", existingUser.getUserEmail(), dto.getEmail());
            existingUser.setUserEmail(dto.getEmail());
            changesMade = true;
        }

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            log.debug("Updating password for user: {}", existingUser.getUserName());
            String encryptedPassword = passwordEncoder.encode(dto.getPassword());
            existingUser.setUserHashPassword(encryptedPassword);
            changesMade = true;
        }

        if (changesMade) {
            log.info("Changes saved for user: {} (ID: {})", existingUser.getUserName(), id);
        } else {
            log.info("No changes detected for user: {} (ID: {})", existingUser.getUserName(), id);
        }

        return userMapper.toDto(existingUser);
    }

    public UserStatusResponse toggleUserStatus(Long userId) {
        log.info("Toggling status for user with ID: {}", userId);

        User user = findUserById(userId);

        boolean newStatus = !user.getUserStatus();
        log.info("Changing status for user {} from {} to {}",
                user.getUserName(), user.getUserStatus(), newStatus);

        user.setUserStatus(newStatus);

        log.info("Status toggled successfully. User {} is now {}",
                user.getUserName(), user.getUserStatus() ? "active" : "inactive");

        return userMapper.toStatusDto(user);
    }

    public UserStatusResponse activateUser(Long userId) {
        log.info("Activating user with ID: {}", userId);

        User user = findUserById(userId);

        // Validate current status
        if (Boolean.TRUE.equals(user.getUserStatus())) {
            log.warn("User {} is already active", user.getUserName());
            throw new UserAlreadyActiveException("User " + user.getUserName() + " is already active");
        }

        log.info("Activating user: {}", user.getUserName());
        user.setUserStatus(true);

        log.info("User activated successfully: {}", user.getUserName());
        return userMapper.toStatusDto(user);
    }

    public UserStatusResponse deactivateUser(Long userId) {
        log.info("Deactivating user with ID: {}", userId);

        User user = findUserById(userId);

        // Validate current status
        if (Boolean.FALSE.equals(user.getUserStatus())) {
            log.warn("User {} is already inactive", user.getUserName());
            throw new UserAlreadyInactiveException("User " + user.getUserName() + " is already inactive");
        }

        log.info("Deactivating user: {}", user.getUserName());
        user.setUserStatus(false);

        log.info("User deactivated successfully: {}", user.getUserName());
        return userMapper.toStatusDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        // Check if user is active
        if (!user.getUserStatus()) {
            log.error("User is disabled: {}", username);
            throw new UsernameNotFoundException("User is disabled: " + username);
        }

        log.info("User found: {} with role: {}", username,
                user.getRol() != null ? user.getRol().getRolName() : "No role defined");

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getUserHashPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String roleName = user.getRol() != null ? user.getRol().getRolName() : "PATIENT";
        String authority = "ROLE_" + roleName.toUpperCase();
        log.debug("Assigning authority: {} to user: {}", authority, user.getUserName());
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

    /// Auxiliar methods
    private User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UserNotFoundException("User with ID " + id + " not found");
                });
    }
}