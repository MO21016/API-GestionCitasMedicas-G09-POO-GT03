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
        System.out.println("Recibido en POST: " + especialidad); // <--- Esto imprime el objeto
        try {
            Especialidad nueva = especialidadService.insertarEspecialidad(especialidad);
            return ResponseEntity.status(201).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

