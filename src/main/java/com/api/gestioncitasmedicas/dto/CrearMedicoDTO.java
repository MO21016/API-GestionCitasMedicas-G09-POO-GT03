package com.api.gestioncitasmedicas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearMedicoDTO {

    @NotBlank(message = "El nombre del médico es obligatorio")
    private String nombreMedico;

    @NotBlank(message = "El apellido del médico es obligatorio")
    private String apellidoMedico;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener exactamente 8 dígitos")
    private String telefonoMedico;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correoMedico;

    // Las especialidades se asignan después con MedicoEspecialidadController
    // No las incluimos aquí para mantener separación de responsabilidades
}