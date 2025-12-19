package com.gestionMedica.main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    /// REFERENCE TO DOCTOR
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
    @Builder.Default
    private Boolean isAvailable = true;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @OneToOne(mappedBy = "availability")
    private Appointments appointments;

    public boolean isTimeSlotAvailable() {
        return isAvailable && appointments == null;
    }

    public boolean overlapsWith(LocalTime otherStart, LocalTime otherEnd) {
        return !startTime.isAfter(otherEnd) && !endTime.isBefore(otherStart);
    }

}
