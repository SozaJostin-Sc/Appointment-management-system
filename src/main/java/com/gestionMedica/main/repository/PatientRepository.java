package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Patient;
import com.gestionMedica.main.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByOrderByPatientIdAsc();
    Optional<Patient> findByUserId(Long id);
}
