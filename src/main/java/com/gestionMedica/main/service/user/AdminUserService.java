package com.gestionMedica.main.service.user;

import com.gestionMedica.main.DTO.user.admin.AdminCreateUser;
import com.gestionMedica.main.DTO.user.admin.AdminUpdateUser;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.entities.Rol;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.repository.RolRepository;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.user.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final UserMapper userMapper;


    /// Obtener todos los usuarios
    public List<UserResponse> getAll(){
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    public UserResponse adminCreate(AdminCreateUser dto) {
        // Verificar si el email ya existe
        if (userRepository.existsByUserEmail(dto.getEmail())) {
            throw new RuntimeException("El email " + dto.getEmail() + " ya está en uso");
        }

        // Verificar si el username ya existe
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new RuntimeException("El nombre de usuario " + dto.getUserName() + " ya está en uso");
        }

        // Validar que el rol existe
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol con id " + dto.getRolId() + " no encontrado"));

        // Encriptar la contraseña
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        // Crear el usuario con el rol específico
        User newUser = User.builder()
                .userName(dto.getUserName())
                .userEmail(dto.getEmail())
                .userHashPassword(encryptedPassword)
                .userStatus(true)
                .dateCreation(LocalDateTime.now())
                .rol(rol)
                .build();

        // Guardar el usuario
        User savedUser = userRepository.save(newUser);

        // Retornar la respuesta
        return userMapper.toDto(savedUser);
    }

    public List<UserResponse> adminCreateMultiple(List<AdminCreateUser> dtos) {
        List<UserResponse> createdUsers = new ArrayList<>();

        for (AdminCreateUser dto : dtos) {
            try {
                UserResponse createdUser = adminCreate(dto);
                createdUsers.add(createdUser);
            } catch (RuntimeException e) {
                // Log the error but continue with other users
                System.err.println("Error creando usuario " + dto.getEmail() + ": " + e.getMessage());
            }
        }

        return createdUsers;
    }

    public UserResponse adminUpdate(Long id, AdminUpdateUser dto) {
        // Buscar el usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado"));

        // Verificar si el email ya existe en otro usuario (solo si se está cambiando)
        if (dto.getEmail() != null &&
                !dto.getEmail().equals(existingUser.getUserEmail()) &&
                userRepository.existsByUserEmail(dto.getEmail())) {
            throw new RuntimeException("El email " + dto.getEmail() + " ya está en uso por otro usuario");
        }

        // Verificar si el username ya existe en otro usuario (solo si se está cambiando)
        if (dto.getUserName() != null &&
                !dto.getUserName().equals(existingUser.getUserName()) &&
                userRepository.existsByUserName(dto.getUserName())) {
            throw new RuntimeException("El nombre de usuario " + dto.getUserName() + " ya está en uso por otro usuario");
        }

        // Actualizar campos si se proporcionan en el DTO
        if (dto.getUserName() != null) {
            existingUser.setUserName(dto.getUserName());
        }

        if (dto.getEmail() != null) {
            existingUser.setUserEmail(dto.getEmail());
        }

        // Actualizar contraseña si se proporciona (los admins pueden forzar cambios de password)
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            if (dto.getPassword().length() < 8) {
                throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
            }
            String encryptedPassword = passwordEncoder.encode(dto.getPassword());
            existingUser.setUserHashPassword(encryptedPassword);
        }

        // Actualizar rol si se proporciona (los admins pueden cambiar roles)
        if (dto.getRolId() != null) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol con id " + dto.getRolId() + " no encontrado"));
            existingUser.setRol(rol);
        }

        // Guardar los cambios
        User updatedUser = userRepository.save(existingUser);

        // Retornar el DTO de respuesta
        return userMapper.toDto(updatedUser);
    }




}
