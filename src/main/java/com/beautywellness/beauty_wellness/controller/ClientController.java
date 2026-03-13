package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.dto.ClientRequestDTO;
import com.beautywellness.beauty_wellness.dto.ClientResponseDTO;
import com.beautywellness.beauty_wellness.mapper.ClientMapper;
import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//controller REST care se ocupa de operatiile asupra clientilor
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    //logica de bussines
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    //returneaza toti clientii
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAll()
                .stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    //returneaza un client dupa ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        return clientService.getById(id)
                .<ResponseEntity<?>>map(c -> ResponseEntity.ok(clientMapper.toResponseDTO(c)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Not Found",
                                "message", "Clientul cu id-ul " + id + " nu a fost găsit"
                        )));
    }

    //returneaza un client dupa email
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClientByEmail(@PathVariable String email) {
        return clientService.getClientByEmail(email)
                .<ResponseEntity<?>>map(c -> ResponseEntity.ok(clientMapper.toResponseDTO(c)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Not Found",
                                "message", "Clientul cu emailul " + email + " nu a fost găsit"
                        )));
    }

    //creeaza un client
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveClient(@RequestBody ClientRequestDTO dto) {
        Client saved = clientService.save(clientMapper.toEntity(dto));
        String message = "Clientul " + saved.getFirstName() + " " + saved.getLastName() + " a fost creat cu succes";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    //actualizeaza un client
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientRequestDTO dto) {
        try {
            Client updated = clientService.update(id, clientMapper.toEntity(dto));
            return ResponseEntity.ok(clientMapper.toResponseDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }

    //sterge un client dupa ID
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.delete(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Clientul cu id-ul " + id + " a fost șters cu succes"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }

    //returneaza toti clientii blocati
    @GetMapping(value = "/blocked", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientResponseDTO>> getBlockedClients() {
        List<ClientResponseDTO> clients = clientService.getBlockedClients()
                .stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }
}