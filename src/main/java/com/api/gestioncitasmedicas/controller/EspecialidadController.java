package com.api.gestioncitasmedicas.controller;
import org.springframework.http.ResponseEntity;
import com.api.gestioncitasmedicas.entity.Especialidad;
import com.api.gestioncitasmedicas.service.EspecialidadService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public List<Especialidad> listarTodas() {
        return especialidadService.listarTodas(); // ✅ correcto
    }

    @PostMapping("/insertar")
    public ResponseEntity<Especialidad> insertar(@RequestBody Especialidad especialidad) {
        System.out.println("Recibido en POST: " + especialidad);
        try {
            Especialidad nueva = especialidadService.guardar(especialidad); // ← aquí
            return ResponseEntity.status(201).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Especialidad> actualizar(
            @PathVariable Long id,
            @RequestBody Especialidad especialidad) {
        System.out.println("Intentando actualizar especialidad con ID: " + id);
        try {
            Especialidad actualizada = especialidadService.actualizarEspecialidad(id, especialidad);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable Long id) {
        try {
            especialidadService.eliminarEspecialidad(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found si no existe
        }
    }


}

