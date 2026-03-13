package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Interfața care gestionează operațiile cu baza de date
@Repository
public interface SalonProcedureRepository extends JpaRepository<SalonProcedure, Long> {

    // Returnează toate serviciile dintr-o anumită categorie
    List<SalonProcedure> findByCategory(ServiceCategory category);
    // Returnează toate serviciile disponibile dintr-o anumită categorie
    List<SalonProcedure> findByAvailableTrueAndCategory(ServiceCategory category);
    // Returnează toate serviciile disponibile
    List<SalonProcedure> findByAvailableTrue();
    // Caută servicii după nume
    List<SalonProcedure> findByNameContainingIgnoreCase(String name);
}