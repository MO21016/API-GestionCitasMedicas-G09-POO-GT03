package com.api.gestioncitasmedicas.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ActualizarPacienteDTO {

    private String nombre;
    private String apellido;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "\\d{8}", message = "El teléfono debe tener exactamente 8 dígitos")
    private String telefono;

    @Email(message = "Correo inválido")
    private String correo;
}
