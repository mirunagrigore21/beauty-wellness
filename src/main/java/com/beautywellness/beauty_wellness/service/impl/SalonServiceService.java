package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.SalonService;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import java.util.List;
import java.util.Optional;

//Interfața care definește operațiile
public interface SalonServiceService {

    //Salvează un serviciu nou în baza de date
    SalonService saveSalonService(SalonService salonService);
    //Returnează toate serviciile din baza de date
    List<SalonService> getAllSalonServices();
    //Caută un serviciu după ID
    Optional<SalonService> getSalonServiceById(Long id);
    //Actualizează datele unui serviciu existent
    SalonService updateSalonService(Long id, SalonService salonService);
    //Șterge un serviciu după ID
    void deleteSalonService(Long id);
    //Returnează toate serviciile dintr-o anumită categorie
    List<SalonService> getServicesByCategory(ServiceCategory category);
    //Returnează toate serviciile disponibile
    List<SalonService> getAvailableServices();
    //Returnează toate serviciile disponibile dintr-o anumită categorie
    List<SalonService> getAvailableServicesByCategory(ServiceCategory category);
    //Caută servicii după nume
    List<SalonService> searchServicesByName(String name);
}