package com.gestionMedica.main.controller.patient;

import com.gestionMedica.main.DTO.patient.request.CreatePatient;
import com.gestionMedica.main.DTO.patient.request.UpdatePatient;
import com.gestionMedica.main.DTO.patient.response.PatientResponse;
import com.gestionMedica.main.DTO.patient.response.PatientStatusResponse;
import com.gestionMedica.main.service.patient.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medical/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    /// OBTENER TODOS LOS PACIENTES
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> patients = patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    /// OBTENER PACIENTE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        PatientResponse patient = patientService.getById(id);
        return ResponseEntity.ok(patient);
    }

    /// COMPLETAR REGISTRO DE PACIENTE
    @PostMapping("/complete")
    public ResponseEntity<PatientResponse> completePatientRegistration(
            @Valid @RequestBody CreatePatient dto) {
        PatientResponse patient = patientService.completePatient(dto);
        return ResponseEntity.ok(patient);
    }

    /// ACTUALIZAR DATOS DE PACIENTE
    @PatchMapping("/{userId}")
    public ResponseEntity<PatientResponse> updatePatient(
            @Valid @RequestBody UpdatePatient dto,
            @PathVariable Long userId) {
        PatientResponse patient = patientService.updatePatient(dto, userId);
        return ResponseEntity.ok(patient);
    }

    /// ACTIVAR PACIENTE
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<PatientStatusResponse> activatePatient(@PathVariable Long userId) {
        PatientStatusResponse response = patientService.activatePatient(userId);
        return ResponseEntity.ok(response);
    }

    /// DESACTIVAR PACIENTE
    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<PatientStatusResponse> deactivatePatient(@PathVariable Long userId) {
        PatientStatusResponse response = patientService.deactivatePatient(userId);
        return ResponseEntity.ok(response);
    }

    /// CAMBIAR ESTADO (TOGGLE) DEL PACIENTE
    @PatchMapping("/{userId}/toggle-status")
    public ResponseEntity<PatientStatusResponse> togglePatientStatus(@PathVariable Long userId) {
        PatientStatusResponse response = patientService.togglePatientStatus(userId);
        return ResponseEntity.ok(response);
    }
}
