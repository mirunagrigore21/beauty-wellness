package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import java.util.List;
import java.util.Optional;

//Interfața care definește operațiile disponibile pentru gestionarea angajaților
public interface EmployeeService {

    //Salvează un angajat nou în baza de date
    Employee saveEmployee(Employee employee);
    //Returnează toți angajații din baza de date
    List<Employee> getAllEmployees();
    //Caută un angajat după ID
    Optional<Employee> getEmployeeById(Long id);
    //Actualizează datele unui angajat existent
    Employee updateEmployee(Long id, Employee employee);
    //Șterge un angajat după ID
    void deleteEmployee(Long id);
    //Caută un angajat după email
    Optional<Employee> getEmployeeByEmail(String email);
    //Returnează toți angajații cu o anumită funcție
    List<Employee> getEmployeesByRole(EmployeeRole role);
    //Returnează toți angajații activi
    List<Employee> getActiveEmployees();
}