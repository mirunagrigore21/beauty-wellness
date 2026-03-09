package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//Clasa care reprezintă un angajat în aplicația- Corespunde tabelului 'employees' din baza de date.

@Data
@Entity
@Table(name = "employees")
public class Employee {
    // Identificatorul unic al angajatului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Prenumele angajatului
    @Column(nullable = false)
    private String firstName;
    //Numele de familie al angajatului
    @Column(nullable = false)
    private String lastName;
    //Adresa de email a angajatului
    @Column(unique = true, nullable = false)
    private String email;
    //Numărul de telefon al angajatului
    @Column(unique = true, nullable = false)
    private String phone;
    //Funcția angajatului
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;
    //Indică dacă angajatul este activ în sistem
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
    //Data și ora înregistrării angajatului în sistem
    private LocalDateTime registeredAt;
    //Metodă apelată automat de JPA înainte de salvarea entității în baza de date
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}