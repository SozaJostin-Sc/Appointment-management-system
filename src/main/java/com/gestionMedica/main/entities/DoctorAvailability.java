package com.gestionMedica.main.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "doctor_availability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Long availabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToOne(mappedBy = "availability")
    private Appointment appointment;

    public boolean isTimeSlotAvailable() {
        return isAvailable && appointment == null;
    }

    public boolean overlapsWith(LocalTime otherStart, LocalTime otherEnd) {
        return !startTime.isAfter(otherEnd) && !endTime.isBefore(otherStart);
    }

}
