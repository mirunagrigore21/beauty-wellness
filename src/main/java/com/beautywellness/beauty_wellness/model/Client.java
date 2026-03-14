package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

//clasa care reprezinta un client în aplicație

@Data
@Entity
@Table(name = "clients")
public class Client {

    //identificatorul unic al clientului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //prenumele clientului
    @Column(nullable = false)
    private String firstName;
    //numele de familie al clientului
    @Column(nullable = false)
    private String lastName;
    //adresa de email a clientului
    @Column(unique = true, nullable = false)
    private String email;
    //numarul de telefon
    @Column(nullable = false, unique = true)
    private String phone;
    //data de naștere a clientului
    private LocalDate birthDate;
    //scorul no-show al clientului pe care il vom monitoriza
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer noShowScore = 0;
    //pentru a vedea daca clientul este blocat pentru un numar prea mare de neprezentari
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean blocked = false;
    //motivul blocarii contului
    @Column(columnDefinition = "TEXT")
    private String blockedReason;
    //mesaj de avertisment pentru 2 no-show-uri
    @Column(columnDefinition = "TEXT")
    private String warningMessage;
    //punctele de fidelitate
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer loyaltyPoints = 0;
    //indica daca clientul are un cupon de reducere disponibil
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean hasCoupon = false;
    //codul cuponului de reducere generat automat
    @Column(unique = true)
    private String couponCode;
    //observatiile suplimentare
    @Column(columnDefinition = "TEXT")
    private String notes;
    //data si ora inregistrarii in sistem
    private LocalDateTime registeredAt;
    //evidenta cand este clientul salvat in baza de date
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}