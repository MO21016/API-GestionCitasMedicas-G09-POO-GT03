package com.api.gestioncitasmedicas.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequest {
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String motivoCita;
    private Long idPaciente;
    private Long idMedico;
    private Long idEspecialidad;
}