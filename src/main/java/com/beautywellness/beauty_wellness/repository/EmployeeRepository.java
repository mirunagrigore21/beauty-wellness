package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

//interfata care gestioneaza operatiile cu baza de date
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //cauta un angajat dupa adresa de email
    Optional<Employee> findByEmail(String email);
    //cauta un angajat dupa numarul de telefon
    Optional<Employee> findByPhone(String phone);
    //returneaza toti angajatii cu o anumită funcție
    List<Employee> findByRole(EmployeeRole role);
    //returneaza toti angajatii activi
    List<Employee> findByActiveTrue();
}