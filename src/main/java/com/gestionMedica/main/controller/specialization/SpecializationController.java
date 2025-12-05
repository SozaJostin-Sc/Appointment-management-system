package com.gestionMedica.main.controller.specialization;

import com.gestionMedica.main.DTO.specialization.request.CreateSpecialization;
import com.gestionMedica.main.DTO.specialization.request.UpdateSpecialization;
import com.gestionMedica.main.DTO.specialization.response.SpecializationResponse;
import com.gestionMedica.main.DTO.specialization.response.SpecializationStatusResponse;
import com.gestionMedica.main.service.specialization.SpecializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical/specializations")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;

    /**
     * Get all specializations
     */
    @GetMapping
    public ResponseEntity<List<SpecializationResponse>> getAllSpecializations() {
        List<SpecializationResponse> specializations = specializationService.getAll();
        return ResponseEntity.ok(specializations);
    }

    /**
     * Get specialization by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecializationResponse> getSpecializationById(@PathVariable Long id) {
        SpecializationResponse specialization = specializationService.getById(id);
        return ResponseEntity.ok(specialization);
    }

    /**
     * Create new specialization
     */
    @PostMapping
    public ResponseEntity<SpecializationResponse> createSpecialization(
            @Valid @RequestBody CreateSpecialization createSpecialization) {
        SpecializationResponse createdSpecialization = specializationService.create(createSpecialization);
        return new ResponseEntity<>(createdSpecialization, HttpStatus.CREATED);
    }

    /**
     * Update specialization
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecializationResponse> updateSpecialization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpecialization updateSpecialization) {
        SpecializationResponse updatedSpecialization = specializationService.update(updateSpecialization, id);
        return ResponseEntity.ok(updatedSpecialization);
    }

    /**
     * Disable specialization
     */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<SpecializationStatusResponse> disableSpecialization(@PathVariable Long id) {
        SpecializationStatusResponse response = specializationService.disable(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Activate specialization
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<SpecializationStatusResponse> activateSpecialization(@PathVariable Long id) {
        SpecializationStatusResponse response = specializationService.activate(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Alternative: Combined status update endpoint
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<SpecializationStatusResponse> updateSpecializationStatus(
            @PathVariable Long id,
            @RequestParam Boolean status) {
        SpecializationStatusResponse response;
        if (Boolean.TRUE.equals(status)) {
            response = specializationService.activate(id);
        } else {
            response = specializationService.disable(id);
        }
        return ResponseEntity.ok(response);
    }
}