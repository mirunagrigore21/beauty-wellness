package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import com.beautywellness.beauty_wellness.repository.EmployeeRepository;
import com.beautywellness.beauty_wellness.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//clasa care implementeaza logica pentru angajaților
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Long> implements EmployeeService {

    //repository utilizat pentru efectuarea operațiilor CRUD
    private final EmployeeRepository employeeRepository;

    //returneaza repository ul specific pentru angajat
    @Override
    protected JpaRepository<Employee, Long> getRepository() {
        return employeeRepository;
    }

    //salveaza un angajat nou
    @Override
    public Employee save(Employee employee) {
        employeeRepository.findByEmail(employee.getEmail())
                .ifPresent(e -> { throw new RuntimeException("Email-ul există deja."); });
        try {
            return employeeRepository.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea angajatului: " + e.getMessage());
        }
    }

    //actualizeaza datele unui angajat
    @Override
    public Employee update(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost găsit."));
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhone(employee.getPhone());
        existingEmployee.setRole(employee.getRole());
        existingEmployee.setActive(employee.getActive());
        try {
            return employeeRepository.save(existingEmployee);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea angajatului: " + e.getMessage());
        }
    }

    //cauta un angajat dupa email
    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    //returneaza toti angajatii cu o functie
    @Override
    public List<Employee> getEmployeesByRole(EmployeeRole role) {
        return employeeRepository.findByRole(role);
    }

    @Override
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findByActiveTrue();
    }
}