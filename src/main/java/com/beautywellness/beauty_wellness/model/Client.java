package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Clasa care reprezintă un client în aplicație - corespunde tabelului 'clients' din baza de date.

@Data
@Entity
@Table(name = "clients")
public class Client {

    //Identificatorul unic al clientului,generat automat de baza de date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Prenumele clientului nu poate fi lasat necompletat
    @Column(nullable = false)
    private String firstName;
    //Numele de familie al clientului nu poate fi lasat necompletat
    @Column(nullable = false)
    private String lastName;
    //Adresa de email a clientului trebuie să fie unică și nu poate fi lasata necompletata
    @Column(unique = true, nullable = false)
    private String email;
    //Numarul de telefon trebuie sa fie completat
    @Column(nullable = false, unique = true)
    private String phone;
    //Data de naștere a clientului este introdusa pentru acordarea reducerii de ziua de naștere
    private LocalDate birthDate;
    //Scorul no-show al clientului pe care il vom monitoriza
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer noShowScore = 0;
    //Pentru a vedea daca clientul este blocat pentru un numar prea mare de neprezentari
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean blocked = false;
    //Punctele de fidelitate a clientului
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer loyaltyPoints = 0;
    //Observatiile suplimentare ale clientului
    @Column(columnDefinition = "TEXT")
    private String notes;
    // Data si ora inregistrarii in sistem a clientului(setata automat pentru prima data)
    private LocalDateTime registeredAt;
    //Evidenta cand este clientul salvat in baza de date
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}