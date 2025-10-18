package com.api.gestioncitasmedicas.controller;

import com.api.gestioncitasmedicas.dto.CitaRequest;
import com.api.gestioncitasmedicas.dto.CitaResponse;
import com.api.gestioncitasmedicas.entity.Cita;
import com.api.gestioncitasmedicas.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    // ✅ Obtener todas las citas
    @GetMapping
    public ResponseEntity<List<CitaResponse>> getAllCitas() {
        try {
            List<CitaResponse> citas = citaService.getAllCitas();
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Obtener cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> getCitaById(@PathVariable Long id) {
        try {
            return citaService.getCitaById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Crear nueva cita
    @PostMapping
    public ResponseEntity<?> createCita(@RequestBody CitaRequest citaRequest) {
        try {
            CitaResponse nuevaCita = citaService.createCita(citaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }

    // ✅ Actualizar estado de cita
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstadoCita(@PathVariable Long id, @RequestParam Cita.EstadoCita estado) {
        try {
            CitaResponse citaActualizada = citaService.updateEstadoCita(id, estado);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }

    // ✅ Cancelar cita
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {
        try {
            CitaResponse citaCancelada = citaService.cancelarCita(id);
            return ResponseEntity.ok(citaCancelada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }

    // ✅ Obtener citas por paciente
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<CitaResponse>> getCitasByPaciente(@PathVariable Long idPaciente) {
        try {
            List<CitaResponse> citas = citaService.getCitasByPaciente(idPaciente);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Obtener citas por médico
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<CitaResponse>> getCitasByMedico(@PathVariable Long idMedico) {
        try {
            List<CitaResponse> citas = citaService.getCitasByMedico(idMedico);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Obtener citas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CitaResponse>> getCitasByEstado(@PathVariable Cita.EstadoCita estado) {
        try {
            List<CitaResponse> citas = citaService.getCitasByEstado(estado);
            return ResponseEntity.ok(citas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Verificar disponibilidad (CORREGIDO)
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> verificarDisponibilidad(
            @RequestParam Long idMedico,
            @RequestParam String fecha,
            @RequestParam String hora) {
        try {
            LocalDate fechaCita = LocalDate.parse(fecha);
            LocalTime horaCita = LocalTime.parse(hora);
            boolean disponible = citaService.verificarDisponibilidad(idMedico, fechaCita, horaCita);
            return ResponseEntity.ok(new DisponibilidadResponse(disponible));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Formato de fecha/hora inválido"));
        }
    }

    // Clases para respuestas
    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class DisponibilidadResponse {
        private boolean disponible;
        public DisponibilidadResponse(boolean disponible) { this.disponible = disponible; }
        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }
    }

    public static class MessageResponse {
        private String message;
        public MessageResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}