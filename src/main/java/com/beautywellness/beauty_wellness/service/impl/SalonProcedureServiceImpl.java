package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import com.beautywellness.beauty_wellness.repository.SalonProcedureRepository;
import com.beautywellness.beauty_wellness.service.SalonProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

//clasa care implementeaza logica pentru gestionarea procedurilor salonului
@Service
@RequiredArgsConstructor
public class SalonProcedureServiceImpl implements SalonProcedureService {

    //repository utilizat pentru efectuarea operatiilor CRUD
    private final SalonProcedureRepository salonProcedureRepository;

    //salveaza o procedura noua
    @Override
    public SalonProcedure saveSalonService(SalonProcedure salonProcedure) {
        try {
            return salonProcedureRepository.save(salonProcedure);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea procedurii: " + e.getMessage());
        }
    }

    //returneaza toate procedurile
    @Override
    public List<SalonProcedure> getAllSalonServices() {
        return salonProcedureRepository.findAll();
    }

    //cauta o procedura dupa id
    @Override
    public Optional<SalonProcedure> getSalonServiceById(Long id) {
        return salonProcedureRepository.findById(id);
    }

    //acualizeaza datele unei proceduri
    @Override
    public SalonProcedure updateSalonService(Long id, SalonProcedure salonProcedure) {
        SalonProcedure existing = salonProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedura nu a fost găsită"));
        existing.setName(salonProcedure.getName());
        existing.setDescription(salonProcedure.getDescription());
        existing.setDurationMinutes(salonProcedure.getDurationMinutes());
        existing.setPrice(salonProcedure.getPrice());
        existing.setCategory(salonProcedure.getCategory());
        existing.setAvailable(salonProcedure.getAvailable());
        try {
            return salonProcedureRepository.save(existing);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea procedurii: " + e.getMessage());
        }
    }

    //sterge o procedura dupa ID
    @Override
    public void deleteSalonService(Long id) {
        try {
            salonProcedureRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la ștergerea procedurii: " + e.getMessage());
        }
    }

    //retuneaza toate procedurile pe categorii
    @Override
    public List<SalonProcedure> getServicesByCategory(ServiceCategory category) {
        return salonProcedureRepository.findByCategory(category);
    }

    //returneaza procedurile disponibile
    @Override
    public List<SalonProcedure> getAvailableServices() {
        return salonProcedureRepository.findByAvailableTrue();
    }

    //returneaza procedurile disponibile pe categorii
    @Override
    public List<SalonProcedure> getAvailableServicesByCategory(ServiceCategory category) {
        return salonProcedureRepository.findByAvailableTrueAndCategory(category);
    }

    //cauta proceduri dupa nume
    @Override
    public List<SalonProcedure> searchServicesByName(String name) {
        return salonProcedureRepository.findByNameContainingIgnoreCase(name);
    }
}