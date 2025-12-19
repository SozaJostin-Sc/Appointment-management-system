package com.gestionMedica.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "Rol")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long rolId;

    @Column(name = "rol_name", nullable = false)
    @NotBlank(message = "Rol name cannot be empty")
    private String rolName;

    @Column(name = "rol_description")
    private String rolDescription;

    @Column(name = "rol_status", nullable = false)
    private Boolean rolStatus = true;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    private Set<User> user = new HashSet<>();


}
