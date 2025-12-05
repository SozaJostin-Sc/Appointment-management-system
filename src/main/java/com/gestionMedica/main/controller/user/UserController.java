package com.gestionMedica.main.controller.user;

import com.gestionMedica.main.DTO.user.RegisterUserDTO;
import com.gestionMedica.main.DTO.user.UpdateUser;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.DTO.user.response.UserStatusResponse;
import com.gestionMedica.main.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/medical/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    /// OBTENER UN USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    /// CREAR USUARIO
    @PostMapping()
    public ResponseEntity<UserResponse> create(@Valid @RequestBody RegisterUserDTO dto){
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    /// ACTUALIZAR USUARIO
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateUser dto, @PathVariable Long id){
        return ResponseEntity.ok(userService.update(dto, id));
    }

    // En UserController
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<UserStatusResponse> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleUserStatus(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserStatusResponse> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserStatusResponse> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }
}


