package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//DTO pentru trimiterea datelor unei programari catre frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    private Long id;
    private ClientResponseDTO client;
    private EmployeeResponseDTO employee;
    private SalonProcedureResponseDTO service;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;
    private Double discount;
}