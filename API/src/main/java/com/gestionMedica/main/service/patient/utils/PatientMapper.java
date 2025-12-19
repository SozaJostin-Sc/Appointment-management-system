package com.gestionMedica.main.service.patient.utils;

import com.gestionMedica.main.DTO.patient.response.PatientResponse;
import com.gestionMedica.main.DTO.patient.response.PatientStatusResponse;
import com.gestionMedica.main.entities.Patient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {
    public PatientResponse toDto(Patient patient){
        if(patient == null) return null;

        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .userId(patient.getUser().getUserId())
                .firstName(patient.getFirstName())
                .secondName(patient.getSecondName())
                .middleName(patient.getMiddleName())
                .lastName(patient.getLastName())
                .dateBirth(patient.getDateBirth())
                .sex(patient.getSex())
                .phoneNumber(patient.getPhoneNumber())
                .build();
    }

    public PatientStatusResponse toStatusDto(Patient patient){
        if(patient == null) return null;

        return PatientStatusResponse.builder()
                .patientId(patient.getPatientId())
                .userId(patient.getUser().getUserId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .status(patient.getUser().getUserStatus())
                .build();
    }

    public List<PatientResponse> toDtoList(List<Patient> patient){
        return patient.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }





}
