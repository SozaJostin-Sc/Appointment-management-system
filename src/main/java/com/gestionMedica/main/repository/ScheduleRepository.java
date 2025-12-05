package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.ScheduleTemplates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleTemplates, Long> {
}
