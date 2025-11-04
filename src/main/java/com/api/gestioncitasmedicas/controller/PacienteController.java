package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.ActualizarPacienteDTO;
import com.api.gestioncitasmedicas.dto.CrearPacienteDTO;
import com.api.gestioncitasmedicas.dto.PacienteDTO;
import com.api.gestioncitasmedicas.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // GET /api/pacientes - Listar todos
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarTodos() {
        List<PacienteDTO> pacientes = pacienteService.obtenerTodos();
        return ResponseEntity.ok(pacientes);
    }

    // GET /api/pacientes/{id} - Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> obtenerPorId(@PathVariable Long id) {
        PacienteDTO paciente = pacienteService.obtenerPorId(id);
        return ResponseEntity.ok(paciente);
    }

    // GET /api/pacientes/buscar?termino=Juan - Buscar por nombre o apellido
    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteDTO>> buscar(@RequestParam String termino) {
        List<PacienteDTO> pacientes = pacienteService.buscarPorNombre(termino);
        return ResponseEntity.ok(pacientes);
    }

    // POST /api/pacientes - Crear nuevo paciente
    @PostMapping
    public ResponseEntity<PacienteDTO> crear(@Valid @RequestBody CrearPacienteDTO dto) {
        PacienteDTO nuevoPaciente = pacienteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
    }

    // PUT /api/pacientes/{id} - Actualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPacienteDTO dto) {
        PacienteDTO pacienteActualizado = pacienteService.actualizar(id, dto);
        return ResponseEntity.ok(pacienteActualizado);
    }

    // DELETE /api/pacientes/{id} - Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}