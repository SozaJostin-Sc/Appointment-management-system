package com.gestionMedica.main.controller.user;

import com.gestionMedica.main.DTO.user.response.UserResponse;
import com.gestionMedica.main.service.user.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/medical/user/admin")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    //OBTENER LA LISTA DE USUARIOS
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAll(){
        return ResponseEntity.ok(adminUserService.getAll());
    }



}
