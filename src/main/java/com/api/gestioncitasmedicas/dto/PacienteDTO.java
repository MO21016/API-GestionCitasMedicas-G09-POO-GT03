package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private Long idPaciente;
    private String nombrePaciente;
    private String apellidoPaciente;
    private LocalDate fechaNacimiento;
    private Integer edad; // ← CALCULADO (no está en BD)
    private String telefonoPaciente;
    private String correoPaciente;
    private Integer cantidadCitas; // ← CALCULADO: cuántas citas tiene
}