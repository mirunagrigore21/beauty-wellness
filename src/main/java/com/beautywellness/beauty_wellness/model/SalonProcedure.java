package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

// Clasa care reprezintă un serviciu oferit de salon

@Data
@Entity
@Table(name = "salon_services")
public class SalonProcedure {

    //Identificatorul unic al serviciului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Numele serviciului
    @Column(nullable = false)
    private String name;
    //Descrierea detaliată a serviciului
    @Column(columnDefinition = "TEXT")
    private String description;
    //Durata serviciului în minute
    @Column(nullable = false)
    private Integer durationMinutes;
    //Prețul serviciului
    @Column(nullable = false)
    private BigDecimal price;
    //Categoria serviciului (Beauty sau Wellness)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;
    //Indică dacă serviciul este disponibil în salon
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean available = true;
}