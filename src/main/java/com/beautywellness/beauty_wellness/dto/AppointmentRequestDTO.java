package com.beautywellness.beauty_wellness.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

//datele trimise pentru crearea unei programari
@Data
public class AppointmentRequestDTO {

    @NotNull(message = "Clientul este obligatoriu")
    private Long clientId;

    @NotNull(message = "Angajatul este obligatoriu")
    private Long employeeId;

    @NotNull(message = "Serviciul este obligatoriu")
    private Long serviceId;

    @NotNull(message = "Data programarii este obligatorie")
    @Future(message = "Data programarii trebuie sa fie in viitor")
    private LocalDateTime appointmentDateTime;

    private String notes;
}