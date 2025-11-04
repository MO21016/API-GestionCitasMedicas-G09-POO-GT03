package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.ActualizarMedicoDTO;
import com.api.gestioncitasmedicas.dto.CrearMedicoDTO;
import com.api.gestioncitasmedicas.dto.MedicoDTO;
import com.api.gestioncitasmedicas.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    // GET /api/medicos - Listar todos
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        List<MedicoDTO> medicos = medicoService.obtenerTodos();
        return ResponseEntity.ok(medicos);
    }

    // GET /api/medicos/{id} - Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> obtenerPorId(@PathVariable Long id) {
        MedicoDTO medico = medicoService.obtenerPorId(id);
        return ResponseEntity.ok(medico);
    }

    // GET /api/medicos/{id}/especialidades - Obtener especialidades del médico
    @GetMapping("/{id}/especialidades")
    public ResponseEntity<List<String>> obtenerEspecialidades(@PathVariable Long id) {
        List<String> especialidades = medicoService.obtenerEspecialidadesDeMedico(id);
        return ResponseEntity.ok(especialidades);
    }

    // POST /api/medicos - Crear nuevo médico
    @PostMapping
    public ResponseEntity<MedicoDTO> crear(@Valid @RequestBody CrearMedicoDTO dto) {
        MedicoDTO nuevoMedico = medicoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMedico);
    }

    // PUT /api/medicos/{id} - Actualizar médico
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarMedicoDTO dto) {
        MedicoDTO medicoActualizado = medicoService.actualizar(id, dto);
        return ResponseEntity.ok(medicoActualizado);
    }

    // DELETE /api/medicos/{id} - Eliminar médico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}