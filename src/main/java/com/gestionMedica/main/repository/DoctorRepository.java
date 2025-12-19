package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByOrderByDoctorIdAsc();

    List<Doctor> findAllByStatusTrueOrderByDoctorIdAsc();

    List<Doctor> findAllByStatusFalseOrderByDoctorIdAsc();

    Optional<Doctor> findByDoctorIdAndStatusTrue(Long id);

    Optional<Doctor> findByDoctorIdAndStatusFalse(Long id);
}
