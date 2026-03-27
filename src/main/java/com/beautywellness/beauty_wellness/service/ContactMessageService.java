package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.dto.ContactMessageRequestDTO;
import com.beautywellness.beauty_wellness.dto.ContactMessageResponseDTO;
import com.beautywellness.beauty_wellness.model.ContactMessageStatus;

import java.util.List;

//interfata pentru gestionarea mesajelor de contact
public interface ContactMessageService {

    //salveaza un mesaj nou
    ContactMessageResponseDTO saveMessage(ContactMessageRequestDTO dto);

    //returneaza toate mesajele
    List<ContactMessageResponseDTO> getAllMessages();

    //returneaza toate mesajele de un anumit status
    List<ContactMessageResponseDTO> getMessagesByStatus(ContactMessageStatus status);

    //actualizeaza statusul unui mesaj
    ContactMessageResponseDTO updateMessageStatus(Long id, ContactMessageStatus status);
}