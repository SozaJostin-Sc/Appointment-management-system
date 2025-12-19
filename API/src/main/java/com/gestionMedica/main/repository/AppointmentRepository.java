package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointments, Long> {
}
