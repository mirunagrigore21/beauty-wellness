package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import java.util.List;
import java.util.Optional;

//interfata care defineste operatiile
public interface SalonProcedureService {

    //salveaza un serviciu nou in baza de date
    SalonProcedure saveSalonService(SalonProcedure salonProcedure);
    //returneaza toate serviciile din baza de date
    List<SalonProcedure> getAllSalonServices();
    //cauta un serviciu dupa ID
    Optional<SalonProcedure> getSalonServiceById(Long id);
    //actualizeaza datele unui serviciu existent
    SalonProcedure updateSalonService(Long id, SalonProcedure salonProcedure);
    //sterge un serviciu dupa ID
    void deleteSalonService(Long id);
    //returneaza toate serviciile dintr-o anumita categorie
    List<SalonProcedure> getServicesByCategory(ServiceCategory category);
    //returneaza toate serviciile disponibile
    List<SalonProcedure> getAvailableServices();
    //returneaza toate serviciile disponibile dintr-o anumita categorie
    List<SalonProcedure> getAvailableServicesByCategory(ServiceCategory category);
    //cauta servicii după nume
    List<SalonProcedure> searchServicesByName(String name);
}