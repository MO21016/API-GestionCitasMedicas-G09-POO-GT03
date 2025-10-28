package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.DTO.*;
import com.api.gestioncitasmedicas.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> crear(@Valid @RequestBody CrearMedicoDTO dto) {
        MedicoDTO creado = medicoService.crear(dto);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarMedicoDTO dto) {
        return ResponseEntity.ok(medicoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

