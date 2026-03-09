package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//Controller REST care se ocupă de operațiile asupra clienților prin cereri HTTP
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    //Introduce serviciul pentru logica de business
    private final ClientService clientService;
    //Returnează toți clienții
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    //Returnează un client după ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Returneaza un client dupa email
    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        return clientService.getClientByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Creează un client nou
    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.saveClient(client));
    }
    //Actualizează un client deja existent
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id,
                                               @RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(id, client));
    }
    //Șterge un client după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    //Returnează toți clienții blocați
    @GetMapping("/blocked")
    public ResponseEntity<List<Client>> getBlockedClients() {
        return ResponseEntity.ok(clientService.getBlockedClients());
    }
}