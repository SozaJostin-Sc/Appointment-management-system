package com.gestionMedica.main.service.rol.util;

import com.gestionMedica.main.DTO.rol.response.RolResponse;
import com.gestionMedica.main.DTO.rol.response.RolStatusResponse;
import com.gestionMedica.main.entities.Rol;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RolMapper {

    /*
    * Convierte una entidad Rol en un DTO de respuesta
    * @param Rol rol, la entidad
    * @return RolResponse, DTO de respuesta
    * */
    public RolResponse toDto(Rol rol){
        if(rol == null){
            return null;
        }

        return RolResponse.builder()
                .rolId(rol.getRolId())
                .rolName(rol.getRolName())
                .rolDescription(rol.getRolDescription())
                .rolStatus(rol.getRolStatus())
                .dateCreation(rol.getDateCreation())
                .build();
    }


    /*
    * Método para convertir una entidad en un dto respuesta de status
    * @param Rol
    * @return RolStatusResponse
    * */
    public RolStatusResponse toStatusDto(Rol rol){
        if(rol == null) return null;

        return RolStatusResponse.builder()
                .rolName(rol.getRolName())
                .rolDescription(rol.getRolDescription())
                .rolStatus(rol.getRolStatus())
                .build();
    }

    /*
    * Convierte una lista de entidades en una lista de dto de respuesta.
    * */
    public List<RolResponse> toDtoList(List<Rol> rolList){
        return rolList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
