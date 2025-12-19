package com.gestionMedica.main.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specialization_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id", nullable = false)
    private Long spId;

    @Column(name = "specialization_name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "specialization_description")
    private String description;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Builder.Default
    @Column(nullable = false)
    private Boolean status = true;

    @ManyToMany(mappedBy = "specializations")
    private Set<Doctor> doctors = new HashSet<>();

}
