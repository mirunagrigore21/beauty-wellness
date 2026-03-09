package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

//Interfața care gestionează operațiile cu baza de date
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //Caută un angajat după adresa de email
    Optional<Employee> findByEmail(String email);
    //Caută un angajat după numărul de telefon
    Optional<Employee> findByPhone(String phone);
    //Returnează toți angajații cu o anumită funcție
    List<Employee> findByRole(EmployeeRole role);
    //Returnează toți angajații activi
    List<Employee> findByActiveTrue();
}