package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.dto.ContactMessageRequestDTO;
import com.beautywellness.beauty_wellness.dto.ContactMessageResponseDTO;
import com.beautywellness.beauty_wellness.mapper.ContactMessageMapper;
import com.beautywellness.beauty_wellness.model.ContactMessage;
import com.beautywellness.beauty_wellness.model.ContactMessageStatus;
import com.beautywellness.beauty_wellness.repository.ContactMessageRepository;
import com.beautywellness.beauty_wellness.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//clasa care implementeaza logica pentru mesajele de contact
@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    //salveaza un mesaj nou
    @Override
    public ContactMessageResponseDTO saveMessage(ContactMessageRequestDTO dto) {
        try {
            ContactMessage contactMessage = contactMessageMapper.toEntity(dto);
            ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
            return contactMessageMapper.toResponseDTO(savedMessage);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea mesajului de contact: " + e.getMessage());
        }
    }

    //returneaza toate mesajele
    @Override
    public List<ContactMessageResponseDTO> getAllMessages() {
        try {
            return contactMessageRepository.findAll()
                    .stream()
                    .map(contactMessageMapper::toResponseDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea mesajelor de contact: " + e.getMessage());
        }
    }

    //returneaza mesajele filtrate dupa status
    @Override
    public List<ContactMessageResponseDTO> getMessagesByStatus(ContactMessageStatus status) {
        try {
            return contactMessageRepository.findByStatus(status)
                    .stream()
                    .map(contactMessageMapper::toResponseDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea mesajelor dupa status: " + e.getMessage());
        }
    }

    //actualizeaza statusul unui mesaj
    @Override
    public ContactMessageResponseDTO updateMessageStatus(Long id, ContactMessageStatus status) {
        ContactMessage contactMessage = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesajul de contact nu a fost gasit"));

        contactMessage.setStatus(status);

        try {
            ContactMessage updatedMessage = contactMessageRepository.save(contactMessage);
            return contactMessageMapper.toResponseDTO(updatedMessage);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea statusului mesajului: " + e.getMessage());
        }
    }
}
