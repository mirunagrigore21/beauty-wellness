package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.EmployeeRole;
import java.util.List;
import java.util.Optional;

//interfata pentru operatiile angajatului

public interface EmployeeService extends BaseService<Employee, Long> {


    Optional<Employee> getEmployeeByEmail(String email);
    List<Employee> getEmployeesByRole(EmployeeRole role);
    List<Employee> getActiveEmployees();
}