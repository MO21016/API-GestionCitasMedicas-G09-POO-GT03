package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de Especialidad: se usa para transferir datos al cliente o recibirlos del mismo.
 * No incluye las listas de médicos o citas para evitar bucles infinitos o sobreexposición.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadDTO {

    private Long idEspecialidad;
    private String nombreEspecialidad;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
