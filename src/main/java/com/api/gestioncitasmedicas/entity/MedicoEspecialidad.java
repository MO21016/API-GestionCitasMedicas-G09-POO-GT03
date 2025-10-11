package com.api.gestioncitasmedicas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medico_especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoEspecialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico_especialidad")
    private Long idMedicoEspecialidad;

    @Column(name = "id_medico", nullable = false)
    private Long idMedico;

    @Column(name = "id_especialidad", nullable = false)
    private Long idEspecialidad;

    @Column(name = "fecha_asignacion", updatable = false)
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void onCreate() {
        fechaAsignacion = LocalDateTime.now();
    }
}