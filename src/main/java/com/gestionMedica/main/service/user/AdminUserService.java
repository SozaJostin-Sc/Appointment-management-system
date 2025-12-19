package com.gestionMedica.main.service.user;

import com.gestionMedica.main.DTO.user.admin.AdminCreateUser;
import com.gestionMedica.main.DTO.user.admin.AdminUpdateUser;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.entities.Rol;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.exceptions.user.UserEmailAlreadyExistException;
import com.gestionMedica.main.exceptions.user.UsernameAlreadyExistException;
import com.gestionMedica.main.exceptions.user.UserNotFoundException;
import com.gestionMedica.main.repository.RolRepository;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.doctor.DoctorService;
import com.gestionMedica.main.service.user.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final UserMapper userMapper;
    private final DoctorService doctorService;

    /// Get all users
    public List<UserResponse> getAll() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return userMapper.toDtoList(users);
    }

    public UserResponse adminCreate(AdminCreateUser dto) {
        log.info("Admin creating new user with username: {}", dto.getUserName());

        // Check if email already exists
        if (userRepository.existsByUserEmail(dto.getEmail())) {
            log.warn("Email already in use: {}", dto.getEmail());
            throw new UserEmailAlreadyExistException("Email " + dto.getEmail() + " is already in use");
        }

        // Check if username already exists
        if (userRepository.existsByUserName(dto.getUserName())) {
            log.warn("Username already in use: {}", dto.getUserName());
            throw new UsernameAlreadyExistException("Username " + dto.getUserName() + " is already in use");
        }

        // Validate that role exists
        Rol rol = findRolById(dto.getRolId());
        log.debug("Found role: {} (ID: {})", rol.getRolName(), dto.getRolId());

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        log.debug("Password encrypted for user: {}", dto.getUserName());

        // Create user with specific role
        User newUser = User.builder()
                .userName(dto.getUserName())
                .userEmail(dto.getEmail())
                .userHashPassword(encryptedPassword)
                .userStatus(true)
                .dateCreation(LocalDateTime.now())
                .rol(rol)
                .build();

        log.info("Created user object: {} with role: {}", dto.getUserName(), rol.getRolName());

        // When we create a user with role "Doctor", create a new default doctor
        if (newUser.getRol().getRolName().equalsIgnoreCase("DOCTOR")) {
            log.info("User has DOCTOR role, creating default doctor profile");
            doctorService.createDefaultDoctor(newUser);
        }

        // Save the user
        User savedUser = userRepository.save(newUser);
        log.info("User created successfully: {} (ID: {})", savedUser.getUserName(), savedUser.getUserId());

        // Return response
        return userMapper.toDto(savedUser);
    }

    public List<UserResponse> adminCreateMultiple(List<AdminCreateUser> dtos) {
        log.info("Admin creating multiple users. Count: {}", dtos.size());
        List<UserResponse> createdUsers = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        for (AdminCreateUser dto : dtos) {
            try {
                log.debug("Processing user creation for: {}", dto.getEmail());
                UserResponse createdUser = adminCreate(dto);
                createdUsers.add(createdUser);
                successCount++;
                log.debug("Successfully created user: {}", dto.getUserName());
            } catch (RuntimeException e) {
                errorCount++;
                log.error("Error creating user {}: {}", dto.getEmail(), e.getMessage(), e);
                // Continue with other users
            }
        }

        log.info("Multiple users creation completed. Success: {}, Failed: {}", successCount, errorCount);
        return createdUsers;
    }

    public UserResponse adminUpdate(Long id, AdminUpdateUser dto) {
        log.info("Admin updating user with ID: {}", id);

        // Find existing user
        User existingUser = findById(id);
        log.info("Found user to update: {} (ID: {})", existingUser.getUserName(), id);

        // Check if email already exists in another user (only if changing)
        if (dto.getEmail() != null &&
                !dto.getEmail().equals(existingUser.getUserEmail()) &&
                userRepository.existsByUserEmail(dto.getEmail())) {
            log.warn("Email already in use by another user: {}", dto.getEmail());
            throw new UserEmailAlreadyExistException("Email " + dto.getEmail() + " is already in use by another user");
        }

        // Check if username already exists in another user (only if changing)
        if (dto.getUserName() != null &&
                !dto.getUserName().equals(existingUser.getUserName()) &&
                userRepository.existsByUserName(dto.getUserName())) {
            log.warn("Username already in use by another user: {}", dto.getUserName());
            throw new UsernameAlreadyExistException("Username " + dto.getUserName() + " is already in use by another user");
        }

        boolean changesMade = false;

        // Update fields if provided in DTO
        if (dto.getUserName() != null && !dto.getUserName().equals(existingUser.getUserName())) {
            log.debug("Updating username from '{}' to '{}'", existingUser.getUserName(), dto.getUserName());
            existingUser.setUserName(dto.getUserName());
            changesMade = true;
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(existingUser.getUserEmail())) {
            log.debug("Updating email from '{}' to '{}'", existingUser.getUserEmail(), dto.getEmail());
            existingUser.setUserEmail(dto.getEmail());
            changesMade = true;
        }

        // Update password if provided (admins can force password changes)
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            if (dto.getPassword().length() < 8) {
                log.warn("Password too short for user: {} (ID: {})", existingUser.getUserName(), id);
                throw new RuntimeException("Password must be at least 8 characters long");
            }
            log.debug("Updating password for user: {}", existingUser.getUserName());
            String encryptedPassword = passwordEncoder.encode(dto.getPassword());
            existingUser.setUserHashPassword(encryptedPassword);
            changesMade = true;
        }

        // Update role if provided (admins can change roles)
        if (dto.getRolId() != null) {
            Rol currentRole = existingUser.getRol();
            if (currentRole == null || !currentRole.getRolId().equals(dto.getRolId())) {
                Rol rol = findRolById(dto.getRolId());
                log.debug("Updating role from '{}' to '{}'",
                        currentRole != null ? currentRole.getRolName() : "null",
                        rol.getRolName());
                existingUser.setRol(rol);
                changesMade = true;
            }
        }

        if (changesMade) {
            log.info("Changes saved for user: {} (ID: {})", existingUser.getUserName(), id);
            // Save changes (needed because we're returning a UserResponse, not the entity)
            userRepository.save(existingUser);
        } else {
            log.info("No changes detected for user: {} (ID: {})", existingUser.getUserName(), id);
        }

        // Return response DTO
        return userMapper.toDto(existingUser);
    }


    /// Utils methods.
    private User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new RuntimeException("User with ID " + id + " not found");
                });
    }

    private Rol findRolById(Long id){
        return rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Role not found with ID: {}", id);
                    return new RuntimeException("Role with ID " + id + " not found");
                });
    }

}