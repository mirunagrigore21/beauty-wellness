package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import java.util.List;
import java.util.Optional;

//Interfața care definește operațiile
public interface SalonProcedureService {

    //Salvează un serviciu nou în baza de date
    SalonProcedure saveSalonService(SalonProcedure salonProcedure);
    //Returnează toate serviciile din baza de date
    List<SalonProcedure> getAllSalonServices();
    //Caută un serviciu după ID
    Optional<SalonProcedure> getSalonServiceById(Long id);
    //Actualizează datele unui serviciu existent
    SalonProcedure updateSalonService(Long id, SalonProcedure salonProcedure);
    //Șterge un serviciu după ID
    void deleteSalonService(Long id);
    //Returnează toate serviciile dintr-o anumită categorie
    List<SalonProcedure> getServicesByCategory(ServiceCategory category);
    //Returnează toate serviciile disponibile
    List<SalonProcedure> getAvailableServices();
    //Returnează toate serviciile disponibile dintr-o anumită categorie
    List<SalonProcedure> getAvailableServicesByCategory(ServiceCategory category);
    //Caută servicii după nume
    List<SalonProcedure> searchServicesByName(String name);
}