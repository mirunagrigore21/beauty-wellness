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

    private String clientName;
    private String employeeName;
    private String serviceName;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;
}