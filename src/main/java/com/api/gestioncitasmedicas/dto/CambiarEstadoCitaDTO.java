package com.api.gestioncitasmedicas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoCitaDTO {

    @NotBlank(message = "El nuevo estado es obligatorio")
    @Pattern(
            regexp = "PENDIENTE|CONFIRMADA|CANCELADA|COMPLETADA",
            message = "Estado inválido. Debe ser: PENDIENTE, CONFIRMADA, CANCELADA o COMPLETADA"
    )
    private String nuevoEstado;
}