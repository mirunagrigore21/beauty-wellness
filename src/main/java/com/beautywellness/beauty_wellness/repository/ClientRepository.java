package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//interfata care lucreaza cu operațiile bazei de date pentru entitatea Client.
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    //cauta un client dupa adresa de email
    Optional<Client> findByEmail(String email);

    //cauta un client dupa numarul de telefon
    Optional<Client> findByPhone(String phone);

    //returneaza toti clientii blocati
    List<Client> findByBlockedTrue();
    //returneaza numarul de clienti noi inregistrati intr-un interval de timp
    Long countByRegisteredAtBetween(LocalDateTime start, LocalDateTime end);
}