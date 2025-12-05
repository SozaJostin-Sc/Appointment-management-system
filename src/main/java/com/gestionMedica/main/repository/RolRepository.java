package com.gestionMedica.main.repository;

import com.gestionMedica.main.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolRepository  extends JpaRepository<Rol, Long> {
    List<Rol> findAllByOrderByRolIdAsc();
    boolean existsByRolNameIgnoreCase(String rolName);
    Rol findByRolNameIgnoreCase(String rolName);

}
