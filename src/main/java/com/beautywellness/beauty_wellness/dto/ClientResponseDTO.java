package com.beautywellness.beauty_wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

//DTO pentru trimiterea datelor unui client catre frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {


    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String notes;
}