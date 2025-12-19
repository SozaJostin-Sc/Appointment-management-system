package com.gestionMedica.main.service.specialization.utils;

import com.gestionMedica.main.DTO.specialization.response.SpecializationResponse;
import com.gestionMedica.main.DTO.specialization.response.SpecializationStatusResponse;
import com.gestionMedica.main.entities.Specialization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecializationMapper {

    /**
     * Convert to dto response
     * @param sp, Specialization entity
     * @return Dto Response
     * */
    public SpecializationResponse toDto(Specialization sp){
        if(sp == null) return null;

        return SpecializationResponse.builder()
                .id(sp.getSpId())
                .name(sp.getName())
                .description(sp.getDescription() != null ? sp.getDescription().trim() : "No description " )
                .dateCreation(sp.getDateCreation())
                .status(sp.getStatus())
                .build();
    }

    public SpecializationStatusResponse toDtoStatus(Specialization sp){
        if(sp == null) return null;

        return SpecializationStatusResponse.builder()
                .id(sp.getSpId())
                .name(sp.getName())
                .status(sp.getStatus())
                .build();
    }
    /**
     * Return a list of dto
     * */
    public List<SpecializationResponse> toDtoList(List<Specialization> spList){
        return spList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
