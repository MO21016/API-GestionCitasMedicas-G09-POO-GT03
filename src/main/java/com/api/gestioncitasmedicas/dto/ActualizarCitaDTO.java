package com.api.gestioncitasmedicas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarCitaDTO {

    @FutureOrPresent(message = "La fecha debe ser hoy o en el futuro")
    private LocalDate fechaCita;

    private LocalTime horaCita;

    private String motivoCita;

    // NO permitimos cambiar: paciente, médico, especialidad
    // Para eso se debe cancelar y crear una nueva cita
}