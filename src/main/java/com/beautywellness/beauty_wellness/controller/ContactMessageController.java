package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.dto.ContactMessageRequestDTO;
import com.beautywellness.beauty_wellness.dto.ContactMessageResponseDTO;
import com.beautywellness.beauty_wellness.model.ContactMessageStatus;
import com.beautywellness.beauty_wellness.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//controller pentru gestionarea mesajelor trimise din formularul de contact
@RestController
@RequestMapping("/api/contact-messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    //salveaza un mesaj nou
    @PostMapping
    public ResponseEntity<ContactMessageResponseDTO> createMessage(@RequestBody ContactMessageRequestDTO dto) {
        return ResponseEntity.ok(contactMessageService.saveMessage(dto));
    }

    //returneaza toate mesajele
    @GetMapping
    public ResponseEntity<List<ContactMessageResponseDTO>> getAllMessages() {
        return ResponseEntity.ok(contactMessageService.getAllMessages());
    }

    //returneaza mesajele dupa status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContactMessageResponseDTO>> getMessagesByStatus(@PathVariable ContactMessageStatus status) {
        return ResponseEntity.ok(contactMessageService.getMessagesByStatus(status));
    }

    //actualizeaza statusul unui mesaj
    @PatchMapping("/{id}/status")
    public ResponseEntity<ContactMessageResponseDTO> updateMessageStatus(
            @PathVariable Long id,
            @RequestParam ContactMessageStatus status) {
        return ResponseEntity.ok(contactMessageService.updateMessageStatus(id, status));
    }
}