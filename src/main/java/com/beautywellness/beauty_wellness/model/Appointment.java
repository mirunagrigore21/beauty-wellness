package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//clasa care reprezinta o programare
@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    //identificatorul unic al programarii
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //clientul care a făcut programarea
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    //angajatul la care s-a facut programarea
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    //serviciul pentru care s-a facut programarea
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private SalonProcedure service;
    //data si ora
    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;
    //statusul programarii
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;
    //observatii suplimentare
    @Column(columnDefinition = "TEXT")
    private String notes;
    //data si ora - creata programarea
    private LocalDateTime createdAt;
    //metoda apelata automat de JPA inainte de salvarea entitații
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}