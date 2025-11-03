package com.api.gestioncitasmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEspecialidadDTO {

    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreEspecialidad;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;
}