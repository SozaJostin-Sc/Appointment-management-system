package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
