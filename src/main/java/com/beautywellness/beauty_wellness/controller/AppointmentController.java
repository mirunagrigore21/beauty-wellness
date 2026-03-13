package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.dto.AppointmentRequestDTO;
import com.beautywellness.beauty_wellness.dto.AppointmentResponseDTO;
import com.beautywellness.beauty_wellness.mapper.AppointmentMapper;
import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import com.beautywellness.beauty_wellness.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//controller REST - operațiile asupra programărilor
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    //serviciul pentru logica de business
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    //returnează toate programările
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments()
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //returnează programare ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .<ResponseEntity<?>>map(a -> ResponseEntity.ok(appointmentMapper.toResponseDTO(a)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Not Found",
                                "message", "Programarea cu id-ul " + id + " nu a fost găsită"
                        )));
    }

    //returneaza toate programarile unui client
    @GetMapping(value = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByClient(@PathVariable Long clientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByClient(clientId)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //returneaza toate programarile unui angajat
    @GetMapping(value = "/employee/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByEmployee(@PathVariable Long employeeId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByEmployee(employeeId)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //returneaza toate programarile cu un anumit status
    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByStatus(status)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //returneaza programarile unui angajat cu un status
    @GetMapping(value = "/employee/{employeeId}/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByEmployeeAndStatus(
            @PathVariable Long employeeId,
            @PathVariable AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByEmployeeAndStatus(employeeId, status)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //returneaza programarile unui client cu un status
    @GetMapping(value = "/client/{clientId}/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByClientAndStatus(
            @PathVariable Long clientId,
            @PathVariable AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByClientAndStatus(clientId, status)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //returneaza toate programarile dintr-un interval de timp
    @GetMapping(value = "/between", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsBetween(start, end)
                .stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    //verifica daca angajatul este disponibil intr-un interval de timp
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isEmployeeAvailable(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.isEmployeeAvailable(employeeId, start, end));
    }

    //creeaza o programare
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveAppointment(@RequestBody AppointmentRequestDTO dto) {
        Appointment saved = appointmentService.saveAppointment(appointmentMapper.toEntity(dto));
        String message = "Programarea din " + saved.getAppointmentDateTime() + " a fost creată cu succes";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    //actualizeaza programarea
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequestDTO dto) {
        try {
            Appointment updated = appointmentService.updateAppointment(id, appointmentMapper.toEntity(dto));
            return ResponseEntity.ok(appointmentMapper.toResponseDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }

    //confirma o programare
    @PatchMapping(value = "/{id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmAppointment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentMapper.toResponseDTO(appointmentService.confirmAppointment(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //clientul anuleaza o programare
    @PatchMapping(value = "/{id}/cancel-client", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelByClient(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentMapper.toResponseDTO(appointmentService.cancelByClient(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //salonul anuleaza o programare
    @PatchMapping(value = "/{id}/cancel-salon", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cancelBySalon(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentMapper.toResponseDTO(appointmentService.cancelBySalon(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //marcheaza o programare ca neprezentata
    @PatchMapping(value = "/{id}/no-show", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markNoShow(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentMapper.toResponseDTO(appointmentService.markNoShow(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //marcheaza o programare ca terminata
    @PatchMapping(value = "/{id}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> completeAppointment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentMapper.toResponseDTO(appointmentService.completeAppointment(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //sterge o programaredupa ID
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Programarea cu id-ul " + id + " a fost ștearsă cu succes"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }
}