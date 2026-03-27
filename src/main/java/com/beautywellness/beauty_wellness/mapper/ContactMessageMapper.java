package com.beautywellness.beauty_wellness.mapper;

import com.beautywellness.beauty_wellness.dto.ContactMessageRequestDTO;
import com.beautywellness.beauty_wellness.dto.ContactMessageResponseDTO;
import com.beautywellness.beauty_wellness.model.ContactMessage;
import org.springframework.stereotype.Component;

//clasa care converteste intre ContactMessage si DTO
@Component
public class ContactMessageMapper {

    //converteste RequestDTO in entity
    public ContactMessage toEntity(ContactMessageRequestDTO dto) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setFullName(dto.getFullName());
        contactMessage.setPhone(dto.getPhone());
        contactMessage.setEmail(dto.getEmail());
        contactMessage.setSubject(dto.getSubject());
        contactMessage.setMessage(dto.getMessage());
        return contactMessage;
    }

    //converteste entity in ResponseDTO
    public ContactMessageResponseDTO toResponseDTO(ContactMessage contactMessage) {
        return ContactMessageResponseDTO.builder()
                .id(contactMessage.getId())
                .fullName(contactMessage.getFullName())
                .phone(contactMessage.getPhone())
                .email(contactMessage.getEmail())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .status(contactMessage.getStatus())
                .createdAt(contactMessage.getCreatedAt())
                .build();
    }
}