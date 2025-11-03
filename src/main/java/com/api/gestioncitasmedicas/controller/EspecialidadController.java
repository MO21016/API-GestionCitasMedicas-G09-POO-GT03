package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.CrearEspecialidadDTO;
import com.api.gestioncitasmedicas.dto.ActualizarEspecialidadDTO;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.service.EspecialidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    // ✅ GET: Listar todas
    @GetMapping
    public List<Especialidad> listarTodas() {
        return especialidadService.listarTodas();
    }

    // ✅ POST: Crear especialidad
    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody CrearEspecialidadDTO dto) {
        Especialidad nueva = especialidadService.crearEspecialidad(dto);
        return ResponseEntity.status(201).body(nueva);
    }

    // ✅ PUT: Actualizar especialidad
    @PutMapping("/{id}")
    public ResponseEntity<Especialidad> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarEspecialidadDTO dto) {
        Especialidad actualizada = especialidadService.actualizarEspecialidad(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // ✅ DELETE: Eliminar especialidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}
