package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.SalonService;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Interfața care gestionează operațiile cu baza de date
@Repository
public interface SalonServiceRepository extends JpaRepository<SalonService, Long> {

    // Returnează toate serviciile dintr-o anumită categorie
    List<SalonService> findByCategory(ServiceCategory category);
    // Returnează toate serviciile disponibile dintr-o anumită categorie
    List<SalonService> findByAvailableTrueAndCategory(ServiceCategory category);
    // Returnează toate serviciile disponibile
    List<SalonService> findByAvailableTrue();
    // Caută servicii după nume
    List<SalonService> findByNameContainingIgnoreCase(String name);
}