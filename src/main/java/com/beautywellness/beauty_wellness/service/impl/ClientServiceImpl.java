package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//Clasa care implementează logica de business pentru gestionarea clienților
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    //Repository utilizat pentru efectuarea operațiilor CRUD asupra entității Client
    private final ClientRepository clientRepository;
    //Salvează un client nou în baza de date
    @Override
    public Client saveClient(Client client) {
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new RuntimeException("Email-ul există deja.");
        }
        return clientRepository.save(client);
    }
    // Returnează toți clienții din baza de date
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    // Caută un client după ID
    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    // Actualizează datele unui client existent
    @Override
    public Client updateClient(Long id, Client client) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clientul nu a fost găsit."));
        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setEmail(client.getEmail());
        existingClient.setPhone(client.getPhone());
        existingClient.setBirthDate(client.getBirthDate());
        existingClient.setNotes(client.getNotes());
        return clientRepository.save(existingClient);
    }
    // Șterge un client după ID
    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Clientul nu a fost găsit.");
        }
        clientRepository.deleteById(id);
    }
    // Caută un client după email
    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    // Caută un client după numărul de telefon
    @Override
    public Optional<Client> getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }
    // Returnează toți clienții blocați
    @Override
    public List<Client> getBlockedClients() {
        return clientRepository.findByBlockedTrue();
    }
}