package com.api.gestioncitasmedicas.DTO;

import lombok.Data;
import java.util.List;

@Data
public class MedicoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private List<String> especialidades; // Solo nombres
}
