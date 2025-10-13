package com.api.gestioncitasmedicas.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import java.util.List;

@Entity
@Table(name = "especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Long idEspecialidad;

    @Column(name = "nombre_especialidad", nullable = false, unique = true, length = 100)
    private String nombreEspecialidad;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    // Relación Many-to-Many con Medico
    @ManyToMany(mappedBy = "especialidades")
    private List<Medico> medicos;

    // Relación One-to-Many con Cita
    @OneToMany(mappedBy = "especialidad")
    private List<Cita> citas;

    // Método para establecer timestamps automáticamente
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