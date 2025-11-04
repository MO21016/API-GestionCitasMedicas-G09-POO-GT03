package com.api.gestioncitasmedicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDTO {
    private Long idMedico;
    private String nombreMedico;
    private String apellidoMedico;
    private String telefonoMedico;
    private String correoMedico;
    private List<String> especialidades; // Lista de nombres de especialidades
    private Integer cantidadEspecialidades; // Calculado: cuántas especialidades tiene
}