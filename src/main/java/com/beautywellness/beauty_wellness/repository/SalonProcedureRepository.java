package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//interfata care gestioneaza operatiile cu baza de date
@Repository
public interface SalonProcedureRepository extends JpaRepository<SalonProcedure, Long> {

    //returneaza toate serviciile dintr-o anumita categorie
    List<SalonProcedure> findByCategory(ServiceCategory category);
    //returneaza toate serviciile disponibile dintr-o anumita categorie
    List<SalonProcedure> findByAvailableTrueAndCategory(ServiceCategory category);
    //returneaza toate serviciile disponibile
    List<SalonProcedure> findByAvailableTrue();
    //cauta servicii dupa nume
    List<SalonProcedure> findByNameContainingIgnoreCase(String name);
}