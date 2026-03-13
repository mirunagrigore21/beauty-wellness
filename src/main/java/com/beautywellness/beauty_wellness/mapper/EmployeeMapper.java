package com.beautywellness.beauty_wellness.mapper;

import com.beautywellness.beauty_wellness.dto.EmployeeRequestDTO;
import com.beautywellness.beauty_wellness.dto.EmployeeResponseDTO;
import com.beautywellness.beauty_wellness.model.Employee;
import org.springframework.stereotype.Component;

//clasa care converteste intre Employee si DTO
@Component
public class EmployeeMapper {

    //converteste RequestDTO - Employee
    public Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setRole(dto.getRole());
        return employee;
    }

    //converteste Employee - ResponseDTO
    public EmployeeResponseDTO toResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .role(employee.getRole())
                .active(employee.getActive())
                .build();
    }
}