package com.gestionMedica.main.entities;

import com.gestionMedica.main.entities.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id", nullable = false, unique = true)
    private Long appointmentId;

    /// Relationship with Patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /// Relationship with Appointments
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "availability_id", nullable = false)
    private DoctorAvailability availability;

    @Column(name = "appointment_start_time",  nullable = false)
    private LocalDateTime appointmentStartTime;

    @Column(name = "appointment_end_time", nullable = false)
    private LocalDateTime appointmentEndTime;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", nullable = false, length = 20)
    @Builder.Default
    private AppointmentStatus appointmentStatus = AppointmentStatus.SCHEDULED;

    @Column(name = "notes")
    private String notes;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (appointmentStartTime != null && appointmentEndTime != null
                && !appointmentStartTime.isBefore(appointmentEndTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }


}
