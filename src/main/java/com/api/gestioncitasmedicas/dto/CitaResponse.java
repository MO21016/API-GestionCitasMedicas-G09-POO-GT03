package com.api.gestioncitasmedicas.dto;

import com.api.gestioncitasmedicas.entity.Cita;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class CitaResponse {
    private Long idCita;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String motivoCita;
    private Cita.EstadoCita estadoCita;
    private String nombrePaciente;
    private String nombreMedico;
    private String nombreEspecialidad;
    private LocalDateTime createdAt;

    public CitaResponse(Cita cita) {
        this.idCita = cita.getIdCita();
        this.fechaCita = cita.getFechaCita();
        this.horaCita = cita.getHoraCita();
        this.motivoCita = cita.getMotivoCita();
        this.estadoCita = cita.getEstadoCita();
        this.nombrePaciente = cita.getPaciente().getNombrePaciente() + " " + cita.getPaciente().getApellidoPaciente();
        this.nombreMedico = cita.getMedico().getNombreMedico() + " " + cita.getMedico().getApellidoMedico();
        this.nombreEspecialidad = cita.getEspecialidad().getNombreEspecialidad();
        this.createdAt = cita.getCreatedAt();
    }
}