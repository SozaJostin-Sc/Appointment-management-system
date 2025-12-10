package com.gestionMedica.main.service.doctor.utils;

import com.gestionMedica.main.DTO.doctor.response.DoctorResponse;
import com.gestionMedica.main.DTO.doctor.response.DoctorStatusResponse;
import com.gestionMedica.main.entities.Doctor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {
    public DoctorResponse toDto(Doctor doctor){
        if(doctor == null) return null;

        return DoctorResponse.builder()
                .userId(doctor.getUser().getUserId())
                .doctorId(doctor.getDoctorId())
                .firstName(doctor.getFirstName())
                .secondName(doctor.getSecondName())
                .middleName(doctor.getMiddleName())
                .lastName(doctor.getLastName())
                .status(doctor.getStatus())
                .specializations(doctor.getSpecializations())
                .build();
    }

    public DoctorStatusResponse toStatusDto(Doctor doctor){
        if(doctor == null) return null;

        return DoctorStatusResponse.builder()
                .userId(doctor.getUser().getUserId())
                .doctorId(doctor.getDoctorId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .status(doctor.getStatus())
                .build();
    }

    public List<DoctorResponse> toDtoList(List<Doctor> doctorList){
        return doctorList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
