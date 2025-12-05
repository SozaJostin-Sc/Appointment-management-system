package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findAllByOrderBySpecializationIdAsc();
    boolean existsByNameIgnoreCase(String name);
}
