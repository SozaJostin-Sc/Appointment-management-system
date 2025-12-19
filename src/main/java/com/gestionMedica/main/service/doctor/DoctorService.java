package com.gestionMedica.main.service.doctor;

import com.gestionMedica.main.DTO.doctor.request.CreateDoctor;
import com.gestionMedica.main.DTO.doctor.request.UpdateDoctor;
import com.gestionMedica.main.DTO.doctor.response.DoctorResponse;
import com.gestionMedica.main.entities.*;
import com.gestionMedica.main.exceptions.doctor.DoctorNotFoundException;
import com.gestionMedica.main.exceptions.global.DTOEmptyException;
import com.gestionMedica.main.repository.DoctorRepository;
import com.gestionMedica.main.repository.SpecializationRepository;
import com.gestionMedica.main.service.doctor.utils.DoctorMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SpecializationRepository specializationRepository;


    /**
     * Create a default doctor
     *
     */
    public void createDefaultDoctor(User user) {
        Doctor doctor = Doctor.builder()
                .user(user)
                .firstName("")
                .secondName("")
                .middleName("")
                .lastName("")
                .build();

        doctorRepository.save(doctor);
    }

    /**
     * Get all doctor.
     *
     */
    public List<DoctorResponse> getAll() {
        return doctorMapper.toDtoList(doctorRepository.findAllByOrderByDoctorIdAsc());
    }

    /**
     * Get by id
     *
     */
    public DoctorResponse getById(Long id) {
        Doctor doctor = getDoctorById(id);
        return doctorMapper.toDto(doctor);
    }

    /**
     * Get active doctors only
     *
     */
    public List<DoctorResponse> getAllActive() {
        return doctorMapper.toDtoList(doctorRepository.findAllByStatusTrueOrderByDoctorIdAsc());
    }

    /**
     * Get inactive doctors only
     *
     */
    public List<DoctorResponse> getAllInactive() {
        return doctorMapper.toDtoList(doctorRepository.findAllByStatusFalseOrderByDoctorIdAsc());
    }

    /**
     * Complete Doctor
     *
     */
    public DoctorResponse completeDoctor(CreateDoctor dto, Long id) {
        if (dto == null) throw new DTOEmptyException("Doctor create dto is empty");

        Doctor doctor = getDoctorById(id);
        Set<Specialization> listSpecialization = findSpecializationsByIds(dto.getSpecializationIds());


        doctor.setFirstName(dto.getFirstName());
        doctor.setSecondName(dto.getSecondName());
        doctor.setMiddleName(dto.getMiddleName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecializations(listSpecialization);

        return doctorMapper.toDto(doctor);
    }


    /**
     * Partial update using UpdateDoctor DTO
     * Actualiza solo los campos que no son nulos
     *
     */
    public DoctorResponse update(UpdateDoctor dto, Long id) {
        if (dto == null) throw new DTOEmptyException("Doctor update dto is empty");

        Doctor doctor = getDoctorById(id);

        // Actualizar solo los campos que no son nulos
        if (dto.getFirstName() != null) {
            doctor.setFirstName(dto.getFirstName());
        }

        if (dto.getSecondName() != null) {
            doctor.setSecondName(dto.getSecondName());
        }

        if (dto.getMiddleName() != null) {
            doctor.setMiddleName(dto.getMiddleName());
        }

        if (dto.getLastName() != null) {
            doctor.setLastName(dto.getLastName());
        }

        if (dto.getSpecializationIds() != null && !dto.getSpecializationIds().isEmpty()) {
            Set<Specialization> listSpecialization = findSpecializationsByIds(dto.getSpecializationIds());
            doctor.setSpecializations(listSpecialization);
        }

        doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    /**
     * Delete doctor (soft delete)
     * Cambia el estado a inactivo
     *
     */
    public void delete(Long id) {
        Doctor doctor = getDoctorById(id);
        doctor.setStatus(false);
        doctorRepository.save(doctor);
    }

    /**
     * Activate doctor
     * Cambia el estado a activo
     *
     */
    public void activate(Long id) {
        Doctor doctor = getDoctorById(id);
        doctor.setStatus(true);
        doctorRepository.save(doctor);
    }


    /**
     * PRIVATE METHODS
     *
     */
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with id: " + id + " not found"));
    }

    public Set<Specialization> findSpecializationsByIds(Set<Long> ids) {
        return new HashSet<>(specializationRepository.findAllById(ids));
    }


}
