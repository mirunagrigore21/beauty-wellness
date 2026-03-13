package com.beautywellness.beauty_wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//DTO pentru primirea datelor unei programari de la frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    private Long clientId;
    private Long employeeId;
    private Long serviceId;
    private LocalDateTime appointmentDateTime;
    private String notes;
}