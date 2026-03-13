package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.dto.SalonProcedureRequestDTO;
import com.beautywellness.beauty_wellness.dto.SalonProcedureResponseDTO;
import com.beautywellness.beauty_wellness.mapper.SalonProcedureMapper;
import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.model.ServiceCategory;
import com.beautywellness.beauty_wellness.service.SalonProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//controller REST pentru operațiile asupra procedurilor salonului
@RestController
@RequestMapping("/api/salon-procedures")
@RequiredArgsConstructor
public class SalonProcedureController {

    //serviciul care gestioneaza logica
    private final SalonProcedureService salonProcedureService;
    private final SalonProcedureMapper salonProcedureMapper;

    //returneaza toate procedurile
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalonProcedureResponseDTO>> getAllServices() {
        List<SalonProcedureResponseDTO> procedures = salonProcedureService.getAllSalonServices()
                .stream()
                .map(salonProcedureMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(procedures);
    }

    //returneaza o procedura dupa ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        return salonProcedureService.getSalonServiceById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(salonProcedureMapper.toResponseDTO(p)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Not Found",
                                "message", "Procedura cu id-ul " + id + " nu a fost găsită"
                        )));
    }

    //returneaza toate procedurile dintr-o categorie
    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalonProcedureResponseDTO>> getServicesByCategory(@PathVariable ServiceCategory category) {
        List<SalonProcedureResponseDTO> procedures = salonProcedureService.getServicesByCategory(category)
                .stream()
                .map(salonProcedureMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(procedures);
    }

    //returnează toate procedurile disponibile
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalonProcedureResponseDTO>> getAvailableServices() {
        List<SalonProcedureResponseDTO> procedures = salonProcedureService.getAvailableServices()
                .stream()
                .map(salonProcedureMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(procedures);
    }

    //returneaza toate procedurile disponibile pe categorii
    @GetMapping(value = "/available/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalonProcedureResponseDTO>> getAvailableServicesByCategory(@PathVariable ServiceCategory category) {
        List<SalonProcedureResponseDTO> procedures = salonProcedureService.getAvailableServicesByCategory(category)
                .stream()
                .map(salonProcedureMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(procedures);
    }

    //cauta proceduri dupa nume
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalonProcedureResponseDTO>> searchServices(@RequestParam String name) {
        List<SalonProcedureResponseDTO> procedures = salonProcedureService.searchServicesByName(name)
                .stream()
                .map(salonProcedureMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(procedures);
    }

    //creează o procedura
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveService(@RequestBody SalonProcedureRequestDTO dto) {
        SalonProcedure saved = salonProcedureService.saveSalonService(salonProcedureMapper.toEntity(dto));
        String message = "Procedura " + saved.getName() + " a fost creată cu succes";
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    //actualizeaza o procedura care exista
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateService(@PathVariable Long id, @RequestBody SalonProcedureRequestDTO dto) {
        try {
            SalonProcedure updated = salonProcedureService.updateSalonService(id, salonProcedureMapper.toEntity(dto));
            return ResponseEntity.ok(salonProcedureMapper.toResponseDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }

    //sterge o programare dupa id
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            salonProcedureService.deleteSalonService(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Procedura cu id-ul " + id + " a fost ștearsă cu succes"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Not Found",
                            "message", e.getMessage()
                    ));
        }
    }
}