package com.api.gestioncitasmedicas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Long idMedico;

    @Column(name = "nombre_medico", nullable = false, length = 100)
    private String nombreMedico;

    @Column(name = "apellido_medico", nullable = false, length = 100)
    private String apellidoMedico;

    @Column(name = "telefono_medico", nullable = false, length = 15)
    private String telefonoMedico;

    @Column(name = "correo_medico", nullable = false, unique = true, length = 100)
    private String correoMedico;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relación Many-to-Many con Especialidad
    @ManyToMany
    @JoinTable(
            name = "medico_especialidad",
            joinColumns = @JoinColumn(name = "id_medico"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidad")
    )
    private List<Especialidad> especialidades;

    // Relación One-to-Many con Cita
    @OneToMany(mappedBy = "medico")
    private List<Cita> citas;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}