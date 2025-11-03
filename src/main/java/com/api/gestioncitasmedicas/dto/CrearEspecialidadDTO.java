package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para crear una nueva especialidad.
 * Solo incluye los campos necesarios para la creación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEspecialidadDTO {

    private String nombreEspecialidad;
    private String descripcion;
}
