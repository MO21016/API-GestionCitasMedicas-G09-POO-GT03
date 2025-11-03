package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.AsignarEspecialidadDTO;
import com.api.gestioncitasmedicas.service.MedicoEspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos-especialidades")
@RequiredArgsConstructor
public class MedicoEspecialidadController {

    private final MedicoEspecialidadService medicoEspecialidadService;

    // POST /api/medicos-especialidades - Asignar especialidad a médico
    @PostMapping
    public ResponseEntity<String> asignar(@Valid @RequestBody AsignarEspecialidadDTO dto) {
        medicoEspecialidadService.asignarEspecialidadAMedico(dto);
        return ResponseEntity.ok("Especialidad asignada correctamente al médico");
    }

    // DELETE /api/medicos-especialidades?idMedico=1&idEspecialidad=2
    @DeleteMapping
    public ResponseEntity<Void> desasignar(
            @RequestParam Long idMedico,
            @RequestParam Long idEspecialidad) {
        medicoEspecialidadService.desasignarEspecialidadDeMedico(idMedico, idEspecialidad);
        return ResponseEntity.noContent().build();
    }
}