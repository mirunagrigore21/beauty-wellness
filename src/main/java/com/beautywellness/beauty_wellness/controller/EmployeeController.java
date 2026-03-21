package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.dto.EmployeeRequestDTO;
import com.beautywellness.beauty_wellness.dto.EmployeeResponseDTO;
import com.beautywellness.beauty_wellness.mapper.EmployeeMapper;
import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import com.beautywellness.beauty_wellness.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//controller care se ocupa de operatiile asupra angajatilor
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    //returneaza toti angajatii
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAll()
                .stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    //returneaza angajatii dupa ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return employeeService.getById(id)
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(employeeMapper.toResponseDTO(e)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Not Found",
                                "message", "Angajatul cu id-ul " + id + " nu a fost găsit"
                        )));
    }

    //retuneaza angajatii dupa email
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email)
                .<ResponseEntity<?>>map(e -> ResponseEntity.ok(employeeMapper.toResponseDTO(e)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Not Found",
                                "message", "Angajatul cu emailul " + email + " nu a fost găsit"
                        )));
    }

    //retuneaza toti angajatii cu o functie
    @GetMapping(value = "/role/{role}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByRole(@PathVariable EmployeeRole role) {
        List<EmployeeResponseDTO> employees = employeeService.getEmployeesByRole(role)
                .stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    //returneaza toti angajatii activi
    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponseDTO>> getActiveEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getActiveEmployees()
                .stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    //creeaza un angajat
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveEmployee(@RequestBody EmployeeRequestDTO dto) {
        Employee saved = employeeService.save(employeeMapper.toEntity(dto));
        String message = "Angajatul " + saved.getFirstName() + " " + saved.getLastName() + " a fost creat cu succes";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    //actualizeaza un angajat
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO dto) {
        try {
            Employee updated = employeeService.update(id, employeeMapper.toEntity(dto));
            return ResponseEntity.ok(employeeMapper.toResponseDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }

    //sterge un angajat dupa ID
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.delete(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Angajatul cu id-ul " + id + " a fost șters cu succes"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }
    //returneaza angajatii disponibili pentru o categorie de serviciu (accesibil si de CLIENT)
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByCategory(@PathVariable String category) {
        List<Employee> employees = employeeService.getAll();

        Map<String, List<String>> categorieRol = new HashMap<>();
        categorieRol.put("HAIR", List.of("HAIR_STYLIST"));
        categorieRol.put("NAILS", List.of("MANICURIST", "PEDICURIST"));
        categorieRol.put("MAKEUP", List.of("MAKEUP_ARTIST"));
        categorieRol.put("SKIN_CARE", List.of("COSMETICIAN"));
        categorieRol.put("EYEBROWS", List.of("EYEBROW_SPECIALIST"));
        categorieRol.put("EYELASHES", List.of("EYELASH_TECHNICIAN"));
        categorieRol.put("MASSAGE", List.of("MASSAGE_THERAPIST"));
        categorieRol.put("SPA", List.of("SPA_THERAPIST"));
        categorieRol.put("AROMATHERAPY", List.of("AROMATHERAPIST"));
        categorieRol.put("REFLEXOLOGY", List.of("REFLEXOLOGIST"));

        List<String> roluri = categorieRol.getOrDefault(category, List.of());

        List<EmployeeResponseDTO> filtrati = employees.stream()
                .filter(e -> roluri.contains(e.getRole().name()))
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filtrati);
    }
}