package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

//clasa care reprezinta un mesaj trimis din formularul de contact
@Data
@Entity
@Table(name = "contact_messages")
public class ContactMessage {

    //identificator unic
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //numele complet al persoanei
    @Column(nullable = false)
    private String fullName;

    //numarul de telefon
    @Column(nullable = false)
    private String phone;

    //adresa de email
    @Column(nullable = false)
    private String email;

    //subiectul mesajului
    @Column(nullable = false)
    private String subject;

    //continutul mesajului
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    //statusul mesajului
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactMessageStatus status;

    //data si ora trimiterii
    private LocalDateTime createdAt;

    //seteaza automat data trimiterii si statusul initial
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = ContactMessageStatus.NEW;
        }
    }
}