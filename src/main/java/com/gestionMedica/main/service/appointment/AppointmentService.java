package com.gestionMedica.main.service.appointment;



import com.gestionMedica.main.DTO.appointment.CreateAppointment;
import com.gestionMedica.main.entities.Appointments;
import com.gestionMedica.main.exceptions.appointment.AppointmentNotFoundException;
import com.gestionMedica.main.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    /// OBTENER TODO
    public List<Appointments> getAll(){
        return appointmentRepository.findAll();
    }

    /// Obtener por id
    public Appointments getById(Long id){
        return appointmentRepository.findById(id)
                .orElseThrow(
                        () -> {
                            return new AppointmentNotFoundException("Appointment with id: " + id + " not exist");
                        });
    }

    @Transactional
    public Appointments create(CreateAppointment dto){
        Appointments appointments = new Appointments();

        return appointments;
    }







}
