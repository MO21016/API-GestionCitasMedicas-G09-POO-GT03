package com.api.gestioncitasmedicas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API Gestión de Citas Médicas funcionando correctamente");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("proyecto", "API REST - Gestión de Citas Médicas");
        info.put("version", "1.0.0");
        info.put("desarrollador", "Equipo G9");
        info.put("descripcion", "Sistema para gestionar citas médicas con validación de disponibilidad");

        return ResponseEntity.ok(info);
    }
}