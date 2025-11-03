package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.ActualizarEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.CrearEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.EspecialidadDTO;
import com.api.gestioncitasmedicas.service.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    // GET /api/especialidades - Listar todas
    @GetMapping
    public ResponseEntity<List<EspecialidadDTO>> listarTodas() {
        List<EspecialidadDTO> especialidades = especialidadService.obtenerTodas();
        return ResponseEntity.ok(especialidades);
    }

    // GET /api/especialidades/{id} - Obtener una por ID
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadDTO> obtenerPorId(@PathVariable Long id) {
        EspecialidadDTO especialidad = especialidadService.obtenerPorId(id);
        return ResponseEntity.ok(especialidad);
    }

    // POST /api/especialidades - Crear nueva especialidad
    @PostMapping
    public ResponseEntity<EspecialidadDTO> crear(@Valid @RequestBody CrearEspecialidadDTO dto) {
        EspecialidadDTO nuevaEspecialidad = especialidadService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEspecialidad);
    }

    // PUT /api/especialidades/{id} - Actualizar especialidad
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEspecialidadDTO dto) {
        EspecialidadDTO especialidadActualizada = especialidadService.actualizar(id, dto);
        return ResponseEntity.ok(especialidadActualizada);
    }

    // DELETE /api/especialidades/{id} - Eliminar especialidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}