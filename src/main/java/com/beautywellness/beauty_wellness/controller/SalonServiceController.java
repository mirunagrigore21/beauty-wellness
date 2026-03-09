package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.model.SalonService;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import com.beautywellness.beauty_wellness.service.SalonServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller REST care se ocupă de operațiile asupra serviciilor salonului prin cereri HTTP
@RestController
@RequestMapping("/api/salon-services")
@RequiredArgsConstructor
public class SalonServiceController {

    //Serviciul care gestionează logica de business
    private final SalonServiceService salonServiceService;
    //Returnează toate serviciile
    @GetMapping
    public ResponseEntity<List<SalonService>> getAllServices() {
        return ResponseEntity.ok(salonServiceService.getAllSalonServices());
    }
    //Returnează un serviciu după ID
    @GetMapping("/{id}")
    public ResponseEntity<SalonService> getServiceById(@PathVariable Long id) {
        return salonServiceService.getSalonServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //Returnează toate serviciile dintr-o anumită categorie
    @GetMapping("/category/{category}")
    public ResponseEntity<List<SalonService>> getServicesByCategory(@PathVariable ServiceCategory category) {
        return ResponseEntity.ok(salonServiceService.getServicesByCategory(category));
    }
    //Returnează toate serviciile disponibile
    @GetMapping("/available")
    public ResponseEntity<List<SalonService>> getAvailableServices() {
        return ResponseEntity.ok(salonServiceService.getAvailableServices());
    }
    //Returnează toate serviciile disponibile dintr-o anumită categorie
    @GetMapping("/available/category/{category}")
    public ResponseEntity<List<SalonService>> getAvailableServicesByCategory(@PathVariable ServiceCategory category) {
        return ResponseEntity.ok(salonServiceService.getAvailableServicesByCategory(category));
    }
    //Caută servicii după nume
    @GetMapping("/search")
    public ResponseEntity<List<SalonService>> searchServices(@RequestParam String name) {
        return ResponseEntity.ok(salonServiceService.searchServicesByName(name));
    }
    //Creează un serviciu nou
    @PostMapping
    public ResponseEntity<SalonService> saveService(@RequestBody SalonService salonService) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salonServiceService.saveSalonService(salonService));
    }
    //Actualizează un serviciu existent
    @PutMapping("/{id}")
    public ResponseEntity<SalonService> updateService(@PathVariable Long id,
                                                      @RequestBody SalonService salonService) {
        return ResponseEntity.ok(salonServiceService.updateSalonService(id, salonService));
    }
    //Șterge un serviciu după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        salonServiceService.deleteSalonService(id);
        return ResponseEntity.noContent().build();
    }
}