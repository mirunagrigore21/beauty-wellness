package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO pentru trimiterea datelor unui angajat catre frontend

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;  // adauga asta
    private EmployeeRole role;
    private Boolean active;
}