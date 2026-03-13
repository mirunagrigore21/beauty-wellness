package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.Client;
import java.util.List;
import java.util.Optional;

//interfata pentru functiile specifice clientului
public interface ClientService extends BaseService<Client, Long> {

    //cauta un client dupa email/numarul de telefon/
    Optional<Client> getClientByEmail(String email);
    Optional<Client> getClientByPhone(String phone);

    //returneaza toți clientii blocati
    List<Client> getBlockedClients();
}