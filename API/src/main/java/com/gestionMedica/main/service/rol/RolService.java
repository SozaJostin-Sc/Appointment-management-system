package com.gestionMedica.main.service.rol;

import com.gestionMedica.main.DTO.rol.request.CreateRolDTO;
import com.gestionMedica.main.DTO.rol.request.UpdateRolDTO;
import com.gestionMedica.main.DTO.rol.response.RolResponse;
import com.gestionMedica.main.DTO.rol.response.RolStatusResponse;
import com.gestionMedica.main.entities.Rol;
import com.gestionMedica.main.exceptions.global.DTOEmptyException;
import com.gestionMedica.main.exceptions.rol.*;
import com.gestionMedica.main.repository.RolRepository;
import com.gestionMedica.main.service.rol.util.RolMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RolService {
    /*
    * Variables privadas
    * */
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;


    /**
    * Método para obtener todos los roles de la base de datos.
    * @return List<RolResponse>
    * */
    public List<RolResponse> getAll() {
        return rolMapper.toDtoList(rolRepository.findAllByOrderByRolIdAsc());
    }

    /**
    * Método para obtener un Rol por ID
    * @param id id, id a buscar
    * @return RolResponse, dto de respuesta.
    * */
    public RolResponse getById(Long id) {
        Rol rol = rolRepository.findById(id).orElseThrow(() -> new RolNotFoundException("Rol con id: " + id + " no encontrado"));
        return rolMapper.toDto(rol);
    }

    /**
    * Método para crear un rol específico.
    * @param dto creación de rol
    * @return RolResponse
    * */
    @Transactional
    public RolResponse create(CreateRolDTO dto) {
        //Validar que sea único
        boolean uniqueRol = rolRepository.existsByRolNameIgnoreCase(dto.getRolName());

        if (uniqueRol) throw new RolAlreadyExistException("Rol with name " + dto.getRolName() + " already exist");

        //Guardamos el rol
        Rol rol = Rol.builder()
                .rolName(dto.getRolName().trim())
                .rolDescription(dto.getRolDescription() != null ? dto.getRolDescription().trim() : "")
                .rolStatus(true)
                .dateCreation(LocalDateTime.now())
                .build();

        rolRepository.save(rol);

        return rolMapper.toDto(rol);
    }

    /**
    * Método de actualización de rol
    * @param id id, UpdateRolDTO dto., id y dto. de actualización
    * @return RolResponse
    * */
    @Transactional
    public RolResponse update(Long id, UpdateRolDTO dto) {
        Rol updateRol = rolRepository.findById(id).orElseThrow(() -> new RolNotFoundException("Rol con id: " + id + " no encontrado"));

        //Validaciones
        if (dto.isEmpty()) throw new DTOEmptyException("Update rol dto cannot be empty");

        if (dto.getRolDescription() != null && !dto.getRolDescription().trim().isEmpty()) {
            updateRol.setRolDescription(dto.getRolDescription().trim());
        }

        if (dto.getRolName() != null && !dto.getRolName().trim().isEmpty()) {
            updateRol.setRolName(dto.getRolName());
        }

        //Guardamos el rol actualizado
        rolRepository.save(updateRol);

        return rolMapper.toDto(updateRol);
    }

    /**
    * Método para desactivar un rol
    * @param id id
    * @return RolStatusResponse
    * */
    @Transactional
    public RolStatusResponse disableRol(Long id) {
        // Busca el rol o lanza RolNotFoundException
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RolNotFoundException("Rol con id: " + id + " no encontrado"));

        if (Boolean.TRUE.equals(rol.getRolStatus())) {
            rol.setRolStatus(false);
            rolRepository.save(rol);
        }

        return rolMapper.toStatusDto(rol);

    }

    /**
    * Método para activar un rol
    * @param id id
    * @return RolStatusResponse
    * */
    @Transactional
    public RolStatusResponse enableRol(Long id) {
        // Busca el rol o lanza RolNotFoundException
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RolNotFoundException("Rol con id: " + id + " no encontrado"));

        // Lógica: Solo activar si actualmente está inactivo
        if (Boolean.FALSE.equals(rol.getRolStatus())) {
            rol.setRolStatus(true);
            rolRepository.save(rol);
        }

        return rolMapper.toStatusDto(rol);
    }

}
