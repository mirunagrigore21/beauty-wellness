package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.model.WorkSchedule;
import com.beautywellness.beauty_wellness.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

//controller REST pentru programul de lucru- angajati
@RestController
@RequestMapping("/api/work-schedules")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    //returneaza programul de lucru al unui angajat
    @GetMapping(value = "/employee/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkSchedule>> getWorkScheduleByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(workScheduleService.getWorkScheduleByEmployee(employeeId));
    }

    //returneaza zilele lucratoare ale unui angajat
    @GetMapping(value = "/employee/{employeeId}/working-days", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkSchedule>> getWorkingDays(@PathVariable Long employeeId) {
        return ResponseEntity.ok(workScheduleService.getWorkingDays(employeeId));
    }

    //adauga un program de lucru
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveWorkSchedule(@RequestBody WorkSchedule workSchedule) {
        WorkSchedule saved = workScheduleService.saveWorkSchedule(workSchedule);
        String message = "Programul de lucru pentru " + saved.getDayOfWeek() + " a fost adaugat cu succes";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    //actualizeaza un program de lucru
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateWorkSchedule(@PathVariable Long id, @RequestBody WorkSchedule workSchedule) {
        try {
            return ResponseEntity.ok(workScheduleService.updateWorkSchedule(id, workSchedule));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //marcheaza o zi - libera
    @PatchMapping(value = "/employee/{employeeId}/day-off/{dayOfWeek}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markDayOff(@PathVariable Long employeeId, @PathVariable DayOfWeek dayOfWeek) {
        try {
            return ResponseEntity.ok(workScheduleService.markDayOff(employeeId, dayOfWeek));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //marcheaza o zi - lucratoare
    @PatchMapping(value = "/employee/{employeeId}/working/{dayOfWeek}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markDayWorking(@PathVariable Long employeeId, @PathVariable DayOfWeek dayOfWeek) {
        try {
            return ResponseEntity.ok(workScheduleService.markDayWorking(employeeId, dayOfWeek));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }

    //sterge un program de lucru
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteWorkSchedule(@PathVariable Long id) {
        try {
            workScheduleService.deleteWorkSchedule(id);
            return ResponseEntity.ok(Map.of("message", "Programul de lucru cu id-ul " + id + " a fost sters cu succes"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", e.getMessage()));
        }
    }
}