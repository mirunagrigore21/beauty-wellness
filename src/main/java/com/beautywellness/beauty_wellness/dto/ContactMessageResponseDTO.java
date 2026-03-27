package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.ContactMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//DTO pentru trimiterea mesajelor de contact catre frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageResponseDTO {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String subject;
    private String message;
    private ContactMessageStatus status;
    private LocalDateTime createdAt;
}