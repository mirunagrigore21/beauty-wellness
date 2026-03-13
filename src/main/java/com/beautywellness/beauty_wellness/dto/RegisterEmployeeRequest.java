package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//datele trimise de admin la crearea unui angajat
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private EmployeeRole employeeRole;
}