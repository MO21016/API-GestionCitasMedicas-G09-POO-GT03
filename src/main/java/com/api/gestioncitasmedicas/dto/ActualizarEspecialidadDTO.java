package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para actualizar una especialidad existente.
 * Solo incluye los campos que pueden modificarse.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEspecialidadDTO {

    private String nombreEspecialidad;
    private String descripcion;
}
