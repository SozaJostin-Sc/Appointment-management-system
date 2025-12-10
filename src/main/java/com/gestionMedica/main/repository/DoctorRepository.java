package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByOrderByDoctorIdAsc();

    List<Doctor> findAllByIsStatusTrueOrderByDoctorIdAsc();

    List<Doctor> findAllByIsStatusFalseOrderByDoctorIdAsc();

    Optional<Doctor> findByIdAndIsStatusTrue(Long id);

    Optional<Doctor> findByIdAndIsStatusFalse(Long id);
}
