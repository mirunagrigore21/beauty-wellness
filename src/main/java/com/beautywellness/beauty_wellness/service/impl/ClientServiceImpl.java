package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//clasa care implementeaza logica pentru gestionare clientilor
@Service
@RequiredArgsConstructor
public class ClientServiceImpl extends BaseServiceImpl<Client, Long> implements ClientService {

    //repository utilizat pentru efectuarea operatiilor pentru clienti
    private final ClientRepository clientRepository;

    //returneaza repository-ul specific
    @Override
    protected JpaRepository<Client, Long> getRepository() {
        return clientRepository;
    }

    //salveaza un client nou
    @Override
    public Client save(Client client) {
        clientRepository.findByEmail(client.getEmail())
                .ifPresent(c -> { throw new RuntimeException("Email-ul există deja."); });
        try {
            return clientRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea clientului: " + e.getMessage());
        }
    }

    //actualizeaza datele unui client care exista
    @Override
    public Client update(Long id, Client client) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clientul nu a fost găsit."));
        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setEmail(client.getEmail());
        existingClient.setPhone(client.getPhone());
        existingClient.setBirthDate(client.getBirthDate());
        existingClient.setNotes(client.getNotes());
        try {
            return clientRepository.save(existingClient);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea clientului: " + e.getMessage());
        }
    }

    //cauta un client dupa email
    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    //cauta un client dupa numarul de telefon
    @Override
    public Optional<Client> getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    //returneaza toti clientii blocați
    @Override
    public List<Client> getBlockedClients() {
        return clientRepository.findByBlockedTrue();
    }
}