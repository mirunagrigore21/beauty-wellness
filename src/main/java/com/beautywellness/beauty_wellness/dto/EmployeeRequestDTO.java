package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO pentru primirea datelor unui angajat de la frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private EmployeeRole role;
}