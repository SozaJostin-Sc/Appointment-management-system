package com.gestionMedica.main.service.specialization;


import com.gestionMedica.main.DTO.specialization.request.CreateSpecialization;
import com.gestionMedica.main.DTO.specialization.request.UpdateSpecialization;
import com.gestionMedica.main.DTO.specialization.response.SpecializationResponse;
import com.gestionMedica.main.DTO.specialization.response.SpecializationStatusResponse;
import com.gestionMedica.main.entities.Specialization;
import com.gestionMedica.main.exceptions.global.DTOEmptyException;
import com.gestionMedica.main.exceptions.specialization.*;
import com.gestionMedica.main.repository.SpecializationRepository;
import com.gestionMedica.main.service.specialization.utils.SpecializationMapper;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;

    /**
     * Get all the specializations
     *
     */
    public List<SpecializationResponse> getAll() {
        return specializationMapper.toDtoList(specializationRepository.findAllByOrderBySpIdAsc());
    }

    /**
     * Get specialization by ID
     *
     */
    public SpecializationResponse getById(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(
                        () -> new SpecializationIdNotFoundException("Specialization with id: " + id + " not found")
                );
        return specializationMapper.toDto(specialization);
    }

    /**
     * Creation
     *
     */
    public SpecializationResponse create(CreateSpecialization dto) {
        uniqueSpecialization(dto.getName());

        Specialization savedSpecialization = Specialization.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription() != null ? dto.getDescription().trim() : "No description")
                .build();

        specializationRepository.save(savedSpecialization);

        return specializationMapper.toDto(savedSpecialization);
    }

    /**
     * Update
     * */
    public SpecializationResponse update(UpdateSpecialization dto, Long id){
        Specialization updateSpecialization = specializationRepository.findById(id)
                .orElseThrow(
                        () -> new SpecializationIdNotFoundException("Specialization with id: " + id + " not found")
                );

        if(dto.dtoIsEmpty()) throw new DTOEmptyException("Specialization update DTO is empty");

        uniqueSpecialization(dto.getName());

        if(dto.getName() != null && !dto.getName().trim().isEmpty()){
            updateSpecialization.setName(dto.getName().trim());
        }

        if(dto.getDescription() != null && !dto.getDescription().trim().isEmpty()){
            updateSpecialization.setDescription(dto.getDescription());
        }

        return specializationMapper.toDto(updateSpecialization);
    }

    /**
     * Disable
     * */
    public SpecializationStatusResponse disable(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() ->
                        new SpecializationIdNotFoundException("Specialization with id: " + id + " not found")
                );

        if (!specialization.getStatus()) {
            throw new StatusAlreadyInactiveException("Specialization with id: " + id + " is already inactive");
        }

        specialization.setStatus(false);
        return specializationMapper.toDtoStatus(specialization);
    }

    /**
     * Activate
     * */
    public SpecializationStatusResponse activate(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() ->
                        new SpecializationIdNotFoundException("Specialization with id: " + id + " not found")
                );

        if (specialization.getStatus()) {
            throw new StatusAlreadyActiveException("Specialization with id: " + id + " is already active");
        }

        specialization.setStatus(true);
        return specializationMapper.toDtoStatus(specialization);
    }



    /**
     * Private methods
     *
     */
    private void uniqueSpecialization(String name){
        boolean specializationUnique = specializationRepository.existsByNameIgnoreCase(name);

        if(specializationUnique) throw new SpecializationAlreadyExistException("Specialization with name" + name + " already exist");
    }


}
