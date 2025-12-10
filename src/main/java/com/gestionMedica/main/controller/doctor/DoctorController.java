package com.gestionMedica.main.controller.doctor;

import com.gestionMedica.main.DTO.doctor.request.CreateDoctor;
import com.gestionMedica.main.DTO.doctor.request.UpdateDoctor;
import com.gestionMedica.main.DTO.doctor.response.DoctorResponse;
import com.gestionMedica.main.service.doctor.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medical/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    /// OBTENER TODOS LOS DOCTORES
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAll();
        return ResponseEntity.ok(doctors);
    }

    /// OBTENER DOCTOR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        DoctorResponse doctor = doctorService.getById(id);
        return ResponseEntity.ok(doctor);
    }

    /// OBTENER DOCTORES ACTIVOS
    @GetMapping("/active")
    public ResponseEntity<List<DoctorResponse>> getActiveDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllActive();
        return ResponseEntity.ok(doctors);
    }

    /// OBTENER DOCTORES INACTIVOS
    @GetMapping("/inactive")
    public ResponseEntity<List<DoctorResponse>> getInactiveDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllInactive();
        return ResponseEntity.ok(doctors);
    }


    /// COMPLETAR REGISTRO DE DOCTOR CON ID ESPECÍFICO
    @PostMapping("/{id}/complete")
    public ResponseEntity<DoctorResponse> completeDoctorRegistrationById(
            @Valid @RequestBody CreateDoctor dto,
            @PathVariable Long id) {
        DoctorResponse doctor = doctorService.completeDoctor(dto, id);
        return ResponseEntity.ok(doctor);
    }

    /// ACTUALIZAR DATOS DE DOCTOR (ACTUALIZACIÓN PARCIAL)
    @PatchMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @Valid @RequestBody UpdateDoctor dto,
            @PathVariable Long id) {
        DoctorResponse doctor = doctorService.update(dto, id);
        return ResponseEntity.ok(doctor);
    }

    /// ACTIVAR DOCTOR
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateDoctor(@PathVariable Long id) {
        doctorService.activate(id);
        return ResponseEntity.ok().build();
    }

    /// DESACTIVAR DOCTOR (SOFT DELETE)
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok().build();
    }

}