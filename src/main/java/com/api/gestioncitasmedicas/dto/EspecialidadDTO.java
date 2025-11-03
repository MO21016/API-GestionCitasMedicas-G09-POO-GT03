package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadDTO {
    private Long idEspecialidad;
    private String nombreEspecialidad;
    private String descripcion;
    private Integer cantidadMedicos; // Calculado: cuántos médicos tienen esta especialidad
}