package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.SalonService;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import com.beautywellness.beauty_wellness.repository.SalonServiceRepository;
import com.beautywellness.beauty_wellness.service.SalonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Clasa care implementează logica de business pentru gestionarea serviciilor salonului
@Service
@RequiredArgsConstructor
public class SalonServiceServiceImpl implements SalonServiceService {

    //Repository utilizat pentru efectuarea operațiilor CRUD asupra entității SalonService
    private final SalonServiceRepository salonServiceRepository;
    //Salvează un serviciu nou în baza de date
    @Override
    public SalonService saveSalonService(SalonService salonService) {
        return salonServiceRepository.save(salonService);
    }
    //Returnează toate serviciile din baza de date
    @Override
    public List<SalonService> getAllSalonServices() {
        return salonServiceRepository.findAll();
    }
    //Caută un serviciu după ID
    @Override
    public Optional<SalonService> getSalonServiceById(Long id) {
        return salonServiceRepository.findById(id);
    }
    //Actualizează datele unui serviciu existent
    @Override
    public SalonService updateSalonService(Long id, SalonService salonService) {
        SalonService existing = salonServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        existing.setName(salonService.getName());
        existing.setDescription(salonService.getDescription());
        existing.setDurationMinutes(salonService.getDurationMinutes());
        existing.setPrice(salonService.getPrice());
        existing.setCategory(salonService.getCategory());
        existing.setAvailable(salonService.getAvailable());
        return salonServiceRepository.save(existing);
    }
    //Șterge un serviciu după ID
    @Override
    public void deleteSalonService(Long id) {
        if (!salonServiceRepository.existsById(id)) {
            throw new RuntimeException("Serviciul nu a fost găsit!");
        }
        salonServiceRepository.deleteById(id);
    }
    //Returnează toate serviciile dintr-o anumită categorie
    @Override
    public List<SalonService> getServicesByCategory(ServiceCategory category) {
        return salonServiceRepository.findByCategory(category);
    }
    //Returnează toate serviciile disponibile
    @Override
    public List<SalonService> getAvailableServices() {
        return salonServiceRepository.findByAvailableTrue();
    }
    //Returnează toate serviciile disponibile dintr-o anumită categorie
    @Override
    public List<SalonService> getAvailableServicesByCategory(ServiceCategory category) {
        return salonServiceRepository.findByAvailableTrueAndCategory(category);
    }
    //Caută servicii după nume
    @Override
    public List<SalonService> searchServicesByName(String name) {
        return salonServiceRepository.findByNameContainingIgnoreCase(name);
    }
}