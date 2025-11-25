package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {
    private Long idCita;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String motivoCita;
    private String estadoCita; // PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA

    // Información del paciente (anidada)
    private PacienteInfoDTO paciente;

    // Información del médico (anidada)
    private MedicoInfoDTO medico;

    // Información de la especialidad (anidada)
    private EspecialidadInfoDTO especialidad;

    // DTOs internos para información resumida
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacienteInfoDTO {
        private Long idPaciente;
        private String nombreCompleto; // "Nombre Apellido"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicoInfoDTO {
        private Long idMedico;
        private String nombreCompleto; // "Dr. Nombre Apellido"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EspecialidadInfoDTO {
        private Long idEspecialidad;
        private String nombreEspecialidad;
    }
}