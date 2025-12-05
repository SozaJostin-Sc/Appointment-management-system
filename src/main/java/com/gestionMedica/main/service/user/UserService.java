package com.gestionMedica.main.service.user;

import com.gestionMedica.main.DTO.user.RegisterUserDTO;
import com.gestionMedica.main.DTO.user.UpdateUser;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.DTO.user.response.UserStatusResponse;
import com.gestionMedica.main.entities.*;
import com.gestionMedica.main.exceptions.user.*;
import com.gestionMedica.main.repository.RolRepository;
import com.gestionMedica.main.repository.UserRepository;
import com.gestionMedica.main.service.patient.PatientService;
import com.gestionMedica.main.service.user.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final UserMapper userMapper;
    private final PatientService patientService;

    /// Obtener por ID
    public UserResponse getById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " no found"));
        return userMapper.toDto(user);
    }

    /// CREACION DE USUARIO
    @Transactional
    public UserResponse create(RegisterUserDTO dto){
        if(userRepository.existsByUserEmail(dto.getEmail())){
            throw new RuntimeException("Ese email ya existe");
        }

        if(userRepository.existsByUserName(dto.getUserName())){
            throw new RuntimeException("Ese usuario ya existe");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        Rol defaultRol = rolRepository.findByRolNameIgnoreCase("patient");

        User newUser = User.builder()
                .userName(dto.getUserName())
                .userEmail(dto.getEmail())
                .userHashPassword(encryptedPassword)
                .userStatus(true)
                .dateCreation(LocalDateTime.now())
                .rol(defaultRol)
                .build();

        patientService.createDefaultPatient(newUser);

        //guardamos el usuario
        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    @Transactional
    public UserResponse update(UpdateUser dto, Long id){
        // Buscar el usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con id " + id + " no encontrado"));

        // Verificar si el email ya existe en otro usuario
        if (dto.getEmail() != null &&
                !dto.getEmail().equals(existingUser.getUserEmail()) &&
                userRepository.existsByUserEmail(dto.getEmail())) {
            throw new RuntimeException("El email " + dto.getEmail() + " ya está en uso");
        }

        // Verificar si el username ya existe en otro usuario
        if (dto.getUserName() != null &&
                !dto.getUserName().equals(existingUser.getUserName()) &&
                userRepository.existsByUserName(dto.getUserName())) {
            throw new RuntimeException("El nombre de usuario " + dto.getUserName() + " ya está en uso");
        }

        // Actualizar campos si se proporcionan en el DTO
        if (dto.getUserName() != null) {
            existingUser.setUserName(dto.getUserName());
        }

        if (dto.getEmail() != null) {
            existingUser.setUserEmail(dto.getEmail());
        }

        // Actualizar contraseña si se proporciona
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(dto.getPassword());
            existingUser.setUserHashPassword(encryptedPassword);
        }

        // Guardar los cambios
        User updatedUser = userRepository.save(existingUser);

        // Retornar el DTO de respuesta
        return userMapper.toDto(updatedUser);
    }

    // En UserService
    @Transactional
    public UserStatusResponse toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        // Cambiar estado
        user.setUserStatus(!user.getUserStatus());

        User updatedUser = userRepository.save(user);
        return userMapper.toStatusDto(updatedUser);
    }

    @Transactional
    public UserStatusResponse activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        // Validar estado actual
        if (Boolean.TRUE.equals(user.getUserStatus())) {
            throw new UserAlreadyActiveException("User " + user.getUserName() + " is already active");
        }

        user.setUserStatus(true);
        User updatedUser = userRepository.save(user);

        return userMapper.toStatusDto(updatedUser);
    }

    @Transactional
    public UserStatusResponse deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        // Validar estado actual
        if (Boolean.FALSE.equals(user.getUserStatus())) {
            throw new UserAlreadyInactiveException("User " + user.getUserName() + " is already inactive");
        }

        user.setUserStatus(false);
        User updatedUser = userRepository.save(user);

        return userMapper.toStatusDto(updatedUser);
    }



}
