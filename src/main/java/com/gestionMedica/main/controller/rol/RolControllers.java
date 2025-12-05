package com.gestionMedica.main.controller.rol;

import com.gestionMedica.main.DTO.rol.request.CreateRolDTO;
import com.gestionMedica.main.DTO.rol.request.UpdateRolDTO;
import com.gestionMedica.main.DTO.rol.response.RolResponse;
import com.gestionMedica.main.DTO.rol.response.RolStatusResponse;
import com.gestionMedica.main.service.rol.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical/roles")
@RequiredArgsConstructor
public class RolControllers {
    /*
     * Variable privada: rolService
     * Permite el acceso a los métodos propios del servicio de rol
     * Manejo de lógica de negocio
     * */
    private final RolService rolService;

    /**
     * Método para obtener todos los roles disponibles.
     * @return ResponseEntity<List<RolResponse>>, Lista de roles con su formato de respuesta
     * */
    @GetMapping()
    public ResponseEntity<List<RolResponse>> getAllRol() {
        return ResponseEntity.ok(rolService.getAll());
    }

    /**
     * Método para obtener un rol con base en un, id
     * @param id id
     * @return ResponseEntity<RolResponse>
     * */
    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> getRolById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.getById(id));
    }

    /**
     * Método para crear un rol
     * @param dto modelo de creación de rol
     * @return ResponseEntity<RolResponse>
     * */
    @PostMapping
    public ResponseEntity<RolResponse> createRol(@Valid @RequestBody CreateRolDTO dto) {
        RolResponse newRol = rolService.create(dto);
        return new ResponseEntity<>(newRol, HttpStatus.CREATED);
    }

    /**
     * Método para actualizar un rol
     *
     * @param id id, UpdateRolDTO dto., modelo de actualización de rol
     * @return ResponseEntity<RolResponse>
     *
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RolResponse> updateRol(@PathVariable Long id, @RequestBody UpdateRolDTO dto) {
        return ResponseEntity.ok(rolService.update(id, dto));
    }

    /**
     * Método para desactivar un rol
     * */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<RolStatusResponse> disableRol(@PathVariable Long id) {
        RolStatusResponse disabledRol = rolService.disableRol(id);
        return ResponseEntity.ok(disabledRol);
    }

    /**
     * Método para activar un rol
     * */
    @PatchMapping("/{id}/enable")
    public ResponseEntity<RolStatusResponse> enableRol(@PathVariable Long id) {
        RolStatusResponse enabledRol = rolService.enableRol(id);
        return ResponseEntity.ok(enabledRol);
    }

}


