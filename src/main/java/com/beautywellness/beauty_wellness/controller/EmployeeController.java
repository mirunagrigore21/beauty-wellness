package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import com.beautywellness.beauty_wellness.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Controller REST care se ocupă de operațiile asupra angajaților prin cereri HTTP
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    //Introduce serviciul pentru logica de business
    private final EmployeeService employeeService;
    //Returnează toți angajații
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    //Returnează un angajat după ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Returnează un angajat după email
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Returnează toți angajații cu o anumită funcție
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Employee>> getEmployeesByRole(@PathVariable EmployeeRole role) {
        return ResponseEntity.ok(employeeService.getEmployeesByRole(role));
    }
    //Returnează toți angajații activi
    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        return ResponseEntity.ok(employeeService.getActiveEmployees());
    }
    //Creează un angajat nou
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.saveEmployee(employee));
    }
    //Actualizează un angajat existent
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                   @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }
    //Șterge un angajat după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}