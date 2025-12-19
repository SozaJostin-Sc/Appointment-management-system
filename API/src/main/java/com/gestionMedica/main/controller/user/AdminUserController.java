package com.gestionMedica.main.controller.user;

import com.gestionMedica.main.DTO.user.admin.AdminCreateUser;
import com.gestionMedica.main.DTO.user.admin.AdminUpdateUser;
import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.service.user.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medical/user/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final AdminUserService adminUserService;

    // GET ALL USERS
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAll() {
        log.info("Admin requested to get all users");
        List<UserResponse> users = adminUserService.getAll();
        log.info("Returning {} users to admin", users.size());
        return ResponseEntity.ok(users);
    }

    // CREATE SINGLE USER
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody AdminCreateUser adminCreateUser) {
        log.info("Admin creating new user with username: {}", adminCreateUser.getUserName());
        UserResponse createdUser = adminUserService.adminCreate(adminCreateUser);
        log.info("User created successfully by admin: {} (ID: {})",
                createdUser.getUserName(), createdUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // CREATE MULTIPLE USERS
    @PostMapping("/batch")
    public ResponseEntity<List<UserResponse>> createMultipleUsers(
            @Valid @RequestBody List<AdminCreateUser> adminCreateUsers) {
        log.info("Admin creating {} users in batch", adminCreateUsers.size());
        List<UserResponse> createdUsers = adminUserService.adminCreateMultiple(adminCreateUsers);
        log.info("Batch creation completed. Created {} users", createdUsers.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsers);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUser adminUpdateUser) {
        log.info("Admin updating user with ID: {}", id);
        UserResponse updatedUser = adminUserService.adminUpdate(id, adminUpdateUser);
        log.info("User updated successfully by admin: {} (ID: {})",
                updatedUser.getUserName(), updatedUser.getUserId());
        return ResponseEntity.ok(updatedUser);
    }

}