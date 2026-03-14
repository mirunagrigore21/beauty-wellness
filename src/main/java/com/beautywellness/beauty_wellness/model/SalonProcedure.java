package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

//clasa care reprezinta un serviciu oferit de salon

@Data
@Entity
@Table(name = "salon_services")
public class SalonProcedure {

    //identificatorul unic al serviciului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //numele
    @Column(nullable = false)
    private String name;
    //descrierea serviciului
    @Column(columnDefinition = "TEXT")
    private String description;
    //durata ( minute )
    @Column(nullable = false)
    private Integer durationMinutes;
    //pretul
    @Column(nullable = false)
    private BigDecimal price;
    //categoria
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;
    //indica daca serviciul este disponibil
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean available = true;
}