package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.Client;
import java.util.List;
import java.util.Optional;

// Interfața care definește operațiile disponibile pentru gestionarea clienților
public interface ClientService {
    // Savează un client nou în baza de date
    Client saveClient(Client client);
    //Returnează toți clienții din baza de date
    List<Client> getAllClients();
    //Caută un client după ID
    Optional<Client> getClientById(Long id);
    //Actualizează datele unui client existent
    Client updateClient(Long id, Client client);
    //Șterge un client după ID
    void deleteClient(Long id);
    //Caută un client după email
    Optional<Client> getClientByEmail(String email);
    //Caută un client după numărul de telefon
    Optional<Client> getClientByPhone(String phone);
    //Returnează toți clienții blocați
    List<Client> getBlockedClients();
}