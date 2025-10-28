package com.api.gestioncitasmedicas.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.List;

@Data
public class ActualizarMedicoDTO {
    private String nombre;
    private String apellido;

    @Pattern(regexp = "\\d{8}", message = "El teléfono debe tener 8 dígitos")
    private String telefono;

    @Email(message = "Formato de correo inválido")
    private String correo;

    private List<Long> especialidadesIds;
}

