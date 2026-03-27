package com.beautywellness.beauty_wellness.dto;

import lombok.Data;

//DTO pentru trimiterea unui mesaj de contact din frontend catre backend
@Data
public class ContactMessageRequestDTO {

    private String fullName;
    private String phone;
    private String email;
    private String subject;
    private String message;
}