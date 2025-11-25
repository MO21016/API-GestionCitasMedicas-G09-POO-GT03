package com.api.gestioncitasmedicas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearCitaDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "El ID del médico es obligatorio")
    private Long idMedico;

    @NotNull(message = "El ID de la especialidad es obligatorio")
    private Long idEspecialidad;

    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser hoy o en el futuro")
    private LocalDate fechaCita;

    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime horaCita;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    private String motivoCita;
}