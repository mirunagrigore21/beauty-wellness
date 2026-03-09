package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import com.beautywellness.beauty_wellness.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

// Controller REST care se ocupă de operațiile asupra programărilor prin cereri HTTP
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    //Introduce serviciul pentru logica de business
    private final AppointmentService appointmentService;
    //Returnează toate programările
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
    //Returnează o programare după ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Returnează toate programările unui client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClient(clientId));
    }
    //Returnează toate programările unui angajat
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByEmployee(employeeId));
    }
    //Returnează toate programările cu un anumit status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }
    //Returnează toate programările unui angajat cu un anumit status
    @GetMapping("/employee/{employeeId}/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByEmployeeAndStatus(
            @PathVariable Long employeeId,
            @PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByEmployeeAndStatus(employeeId, status));
    }
    //Returnează toate programările unui client cu un anumit status
    @GetMapping("/client/{clientId}/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByClientAndStatus(
            @PathVariable Long clientId,
            @PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClientAndStatus(clientId, status));
    }
    //Returnează toate programările dintr-un interval de timp
    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> getAppointmentsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.getAppointmentsBetween(start, end));
    }
    //Verifică dacă angajatul este disponibil într-un interval de timp
    @GetMapping("/available")
    public ResponseEntity<Boolean> isEmployeeAvailable(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.isEmployeeAvailable(employeeId, start, end));
    }
    //Creează o programare nouă
    @PostMapping
    public ResponseEntity<Appointment> saveAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.saveAppointment(appointment));
    }
    //Actualizează o programare existentă
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id,
                                                         @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointment));
    }
    //Confirmă o programare
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Appointment> confirmAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(id));
    }
    // Anulează o programare de către client
    @PatchMapping("/{id}/cancel-client")
    public ResponseEntity<Appointment> cancelByClient(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelByClient(id));
    }
    // Anulează o programare de către salon
    @PatchMapping("/{id}/cancel-salon")
    public ResponseEntity<Appointment> cancelBySalon(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelBySalon(id));
    }
    //Marchează o programare ca neprezentare
    @PatchMapping("/{id}/no-show")
    public ResponseEntity<Appointment> markNoShow(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.markNoShow(id));
    }
    //Marchează o programare ca finalizată
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Appointment> completeAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }
    //Șterge o programare după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}