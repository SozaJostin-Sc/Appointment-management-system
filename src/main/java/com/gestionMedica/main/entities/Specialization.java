package com.gestionMedica.main.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "date_creation", nullable = false)
    @Builder.Default
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private Boolean status = true;

    @ManyToMany(mappedBy = "specializations")
    private Set<Doctor> doctors = new HashSet<>();

}
