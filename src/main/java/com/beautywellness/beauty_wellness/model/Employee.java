package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//clasa care reprezinta un angajat in aplicatia

@Data
@Entity
@Table(name = "employees")
public class Employee {
    //identificatorul unic al angajatului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //prenumele
    @Column(nullable = false)
    private String firstName;
    //numele de familie
    @Column(nullable = false)
    private String lastName;
    //adresa de email
    @Column(unique = true, nullable = false)
    private String email;
    //numarul de telefon
    @Column(unique = true, nullable = false)
    private String phone;
    //functia angajatului
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;
    //indică daca angajatul este activ
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
    //data si ora inregistrarii angajatului
    private LocalDateTime registeredAt;
    //metoda apelata automat de JPA inainte de salvarea entitatii
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}