package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//Clasa care reprezintă o programare în aplicația
@Data
@Entity
@Table(name = "appointments")
public class Appointment {
    //Identificatorul unic al programării
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Clientul care a făcut programarea
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    //Angajatul la care s-a făcut programarea
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    //Serviciul pentru care s-a făcut programarea
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private SalonService service;
    //Data și ora programării
    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;
    //Statusul programării
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;
    //Observații suplimentare
    @Column(columnDefinition = "TEXT")
    private String notes;
    // Data și ora la care a fost creată programarea
    private LocalDateTime createdAt;
    // Metodă apelată automat de JPA înainte de salvarea entității în baza de date
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}