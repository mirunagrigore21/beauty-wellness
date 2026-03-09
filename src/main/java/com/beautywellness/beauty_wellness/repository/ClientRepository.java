package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Interfata care lucreaza cu operațiile bazei de date pentru entitatea Client.
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Caută un client după adresa de email
    Optional<Client> findByEmail(String email);

    // Caută un client după numărul de telefon
    Optional<Client> findByPhone(String phone);

    // Returnează toți clienții blocați
    List<Client> findByBlockedTrue();
}