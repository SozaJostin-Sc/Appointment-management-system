package com.gestionMedica.main.service.patient;

import com.gestionMedica.main.DTO.patient.request.CreatePatient;
import com.gestionMedica.main.DTO.patient.request.UpdatePatient;
import com.gestionMedica.main.DTO.patient.response.PatientResponse;
import com.gestionMedica.main.DTO.patient.response.PatientStatusResponse;
import com.gestionMedica.main.entities.Patient;
import com.gestionMedica.main.entities.User;
import com.gestionMedica.main.exceptions.global.DTOEmptyException;
import com.gestionMedica.main.exceptions.patient.PatientNotFoundException;
import com.gestionMedica.main.repository.PatientRepository;
import com.gestionMedica.main.service.patient.utils.PatientMapper;
import com.gestionMedica.main.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserService userService;

    // Método auxiliar para buscar paciente por ID
    private Patient findPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id: " + id + " not found"));
    }

    // Método auxiliar para buscar paciente por userId
    private Patient findPatientByUserId(Long userId) {
        return patientRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with user id: " + userId + " not found"));
    }

    public List<PatientResponse> getAll(){
        return patientMapper.toDtoList(patientRepository.findAllByOrderByPatientIdAsc());
    }

    public PatientResponse getById(Long id){
        Patient patient = findPatientById(id);
        return patientMapper.toDto(patient);
    }

    public void createDefaultPatient(User user){
        Patient newPatient = Patient.builder()
                .user(user)
                .firstName(user.getUserName().trim())
                .secondName(null)
                .middleName(null)
                .lastName("defaultLastName")
                .dateBirth(LocalDate.of(
                        2000,
                        5,
                        10
                ))
                .sex('O')
                .phoneNumber("50578398013")
                .build();

        patientRepository.save(newPatient);
    }

    public PatientResponse completePatient(CreatePatient dto){
        if(dto == null) throw new DTOEmptyException("Patient creation dto is empty");

        Patient patient = findPatientByUserId(dto.getUserId());

        patient.setFirstName(dto.getFirstName());
        patient.setSecondName(dto.getSecondName());
        patient.setMiddleName(dto.getMiddleName());
        patient.setLastName(dto.getLastName());
        patient.setDateBirth(dto.getDateBirth());
        patient.setSex(dto.getSex().toUpperCase().charAt(0));
        patient.setPhoneNumber(dto.getPhoneNumber());

        return patientMapper.toDto(patient);
    }

    public PatientResponse updatePatient(UpdatePatient dto, Long id) {
        if (dto == null) throw new DTOEmptyException("Patient update dto is empty");

        Patient patient = findPatientByUserId(id);

        if (dto.getFirstName() != null) {
            patient.setFirstName(dto.getFirstName());
        }

        if (dto.getSecondName() != null) {
            patient.setSecondName(dto.getSecondName());
        }

        if (dto.getMiddleName() != null) {
            patient.setMiddleName(dto.getMiddleName());
        }

        if (dto.getLastName() != null) {
            patient.setLastName(dto.getLastName());
        }

        if (dto.getDateBirth() != null) {
            patient.setDateBirth(dto.getDateBirth());
        }

        if (dto.getSex() != null && !dto.getSex().trim().isEmpty()) {
            patient.setSex(dto.getSex().toUpperCase().charAt(0));
        }

        if (dto.getPhoneNumber() != null) {
            patient.setPhoneNumber(dto.getPhoneNumber());
        }

        return patientMapper.toDto(patient);
    }

    public PatientStatusResponse togglePatientStatus(Long id){
        Patient patient = findPatientByUserId(id);
        userService.toggleUserStatus(id);
        return patientMapper.toStatusDto(patient);
    }

    public PatientStatusResponse activatePatient(Long id){
        Patient patient = findPatientByUserId(id);
        userService.activateUser(id);
        return patientMapper.toStatusDto(patient);
    }

    public PatientStatusResponse deactivatePatient(Long id){
        Patient patient = findPatientByUserId(id);
        userService.deactivateUser(id);
        return patientMapper.toStatusDto(patient);
    }
}