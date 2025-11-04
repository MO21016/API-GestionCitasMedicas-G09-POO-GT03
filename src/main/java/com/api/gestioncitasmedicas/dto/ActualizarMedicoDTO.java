package com.api.gestioncitasmedicas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarMedicoDTO {

    private String nombreMedico;

    private String apellidoMedico;

    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener exactamente 8 dígitos")
    private String telefonoMedico;

    @Email(message = "Formato de correo inválido")
    private String correoMedico;

    // Las especialidades se gestionan con MedicoEspecialidadController
    // No las incluimos aquí
}