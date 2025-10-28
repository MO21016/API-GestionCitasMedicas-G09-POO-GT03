package com.api.gestioncitasmedicas.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class CrearMedicoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El teléfono debe tener 8 dígitos")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;

    @NotEmpty(message = "Debe asignar al menos una especialidad")
    private List<Long> especialidadesIds;
}

