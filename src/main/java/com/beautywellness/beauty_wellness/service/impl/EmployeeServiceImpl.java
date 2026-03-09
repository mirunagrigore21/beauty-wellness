package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import com.beautywellness.beauty_wellness.repository.EmployeeRepository;
import com.beautywellness.beauty_wellness.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//Clasa care implementează logica de business pentru gestionarea angajaților
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    //Repository utilizat pentru efectuarea operațiilor
    private final EmployeeRepository employeeRepository;
    //Salvează un angajat nou în baza de date
    @Override
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new RuntimeException("Email-ul există deja");
        }
        return employeeRepository.save(employee);
    }
    //Returnează toți angajații din baza de date
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    //Caută un angajat după ID
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    //Actualizează datele unui angajat existent
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost găsit."));
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhone(employee.getPhone());
        existingEmployee.setRole(employee.getRole());
        existingEmployee.setActive(employee.getActive());
        return employeeRepository.save(existingEmployee);
    }
    //Șterge un angajat după ID
    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Angajatul nu a fost găsit.");
        }
        employeeRepository.deleteById(id);
    }
    //Caută un angajat după email
    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    //Returnează toți angajații cu o anumită funcție
    @Override
    public List<Employee> getEmployeesByRole(EmployeeRole role) {
        return employeeRepository.findByRole(role);
    }
    //Returnează toți angajații activi
    @Override
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByActiveTrue();
    }
}