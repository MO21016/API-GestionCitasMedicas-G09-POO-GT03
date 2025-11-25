package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.ActualizarCitaDTO;
import com.api.gestioncitasmedicas.dto.CambiarEstadoCitaDTO;
import com.api.gestioncitasmedicas.dto.CitaDTO;
import com.api.gestioncitasmedicas.dto.CrearCitaDTO;
import com.api.gestioncitasmedicas.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    // GET /api/citas - Listar todas las citas
    // GET /api/citas?medico=1 - Filtrar por médico
    // GET /api/citas?paciente=1 - Filtrar por paciente
    // GET /api/citas?estado=PENDIENTE - Filtrar por estado
    @GetMapping
    public ResponseEntity<List<CitaDTO>> listarOFiltrar(
            @RequestParam(required = false) Long medico,
            @RequestParam(required = false) Long paciente,
            @RequestParam(required = false) String estado) {

        List<CitaDTO> citas = citaService.filtrar(medico, paciente, estado);
        return ResponseEntity.ok(citas);
    }

    // GET /api/citas/{id} - Obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> obtenerPorId(@PathVariable Long id) {
        CitaDTO cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(cita);
    }

    // POST /api/citas - Crear nueva cita (con todas las validaciones)
    @PostMapping
    public ResponseEntity<CitaDTO> crear(@Valid @RequestBody CrearCitaDTO dto) {
        CitaDTO nuevaCita = citaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    // PUT /api/citas/{id} - Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCitaDTO dto) {
        CitaDTO citaActualizada = citaService.actualizar(id, dto);
        return ResponseEntity.ok(citaActualizada);
    }

    // PATCH /api/citas/{id}/estado - Cambiar solo el estado de la cita
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoCitaDTO dto) {
        CitaDTO citaActualizada = citaService.cambiarEstado(id, dto.getNuevoEstado());
        return ResponseEntity.ok(citaActualizada);
    }

    // DELETE /api/citas/{id} - Eliminar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}