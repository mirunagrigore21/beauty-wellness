package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.repository.AppointmentRepository;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Clasa care implementează logica pentru gestionarea programărilor
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    //Repository pentru operațiile CRUD asupra programărilor
    private final AppointmentRepository appointmentRepository;

    //Repository pentru actualizarea datelor clientului (no-show, blocare)
    private final ClientRepository clientRepository;

    //Salvează o programare nouă după verificarea disponibilității angajatului
    @Override
    public Appointment saveAppointment(Appointment appointment) {
        LocalDateTime start = appointment.getAppointmentDateTime();
        LocalDateTime end = start.plusMinutes(appointment.getService().getDurationMinutes());
        if (!isEmployeeAvailable(appointment.getEmployee().getId(), start, end)) {
            throw new RuntimeException("Angajatul nu este disponibil în intervalul selectat");
        }
        return appointmentRepository.save(appointment);
    }

    //Returnează toate programările din baza de date
    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    //Caută o programare după ID
    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    //Actualizează datele unei programări existente
    //Verifică disponibilitatea angajatului pentru noul interval
    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));

        LocalDateTime start = appointment.getAppointmentDateTime();
        LocalDateTime end = start.plusMinutes(appointment.getService().getDurationMinutes());

        if (!isEmployeeAvailable(appointment.getEmployee().getId(), start, end)) {
            throw new RuntimeException("Angajatul nu este disponibil în intervalul selectat");
        }

        existing.setClient(appointment.getClient());
        existing.setEmployee(appointment.getEmployee());
        existing.setService(appointment.getService());
        existing.setAppointmentDateTime(appointment.getAppointmentDateTime());
        existing.setNotes(appointment.getNotes());
        return appointmentRepository.save(existing);
    }

    //Șterge o programare după ID
    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Programarea nu a fost găsită");
        }
        appointmentRepository.deleteById(id);
    }

    //Returnează toate programările unui client
    @Override
    public List<Appointment> getAppointmentsByClient(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    //Returnează toate programările unui angajat
    @Override
    public List<Appointment> getAppointmentsByEmployee(Long employeeId) {
        return appointmentRepository.findByEmployeeId(employeeId);
    }

    //Returnează toate programările cu un anumit status
    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    //Returnează toate programările dintr-un interval de timp
    @Override
    public List<Appointment> getAppointmentsBetween(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }

    //Returnează toate programările unui angajat cu un anumit status
    @Override
    public List<Appointment> getAppointmentsByEmployeeAndStatus(Long employeeId, AppointmentStatus status) {
        return appointmentRepository.findByEmployeeIdAndStatus(employeeId, status);
    }

    //Returnează toate programările unui client cu un anumit status
    @Override
    public List<Appointment> getAppointmentsByClientAndStatus(Long clientId, AppointmentStatus status) {
        return appointmentRepository.findByClientIdAndStatus(clientId, status);
    }

    //Verifică dacă angajatul este disponibil în intervalul de timp dat
    //Folosește verificare reală de suprapunere a intervalelor
    @Override
    public boolean isEmployeeAvailable(Long employeeId, LocalDateTime start, LocalDateTime end) {
        List<AppointmentStatus> excludedStatuses = List.of(
                AppointmentStatus.CANCELLED_BY_CLIENT,
                AppointmentStatus.CANCELLED_BY_SALON,
                AppointmentStatus.NO_SHOW
        );
        List<Appointment> overlapping = appointmentRepository.findOverlappingAppointments(
                employeeId, start, end, excludedStatuses
        );
        return overlapping.isEmpty();
    }

    //Confirmă o programare
    @Override
    public Appointment confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        return appointmentRepository.save(appointment);
    }

    //Anulează o programare de către client
    @Override
    public Appointment cancelByClient(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_CLIENT);
        return appointmentRepository.save(appointment);
    }

    //Anulează o programare de către salon
    @Override
    public Appointment cancelBySalon(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_SALON);
        return appointmentRepository.save(appointment);
    }

    //Marchează o programare ca neprezentare
    //Crește scorul no-show al clientului și îl blochează dacă ajunge la 3
    @Override
    public Appointment markNoShow(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.NO_SHOW);

        // Actualizează scorul no-show al clientului
        Client client = appointment.getClient();
        int newScore = client.getNoShowScore() + 1;
        client.setNoShowScore(newScore);

        // Blochează clientul dacă are 3 sau mai multe neprezentări
        if (newScore >= 3) {
            client.setBlocked(true);
        }
        clientRepository.save(client);

        return appointmentRepository.save(appointment);
    }

    //Marchează o programare ca finalizată
    @Override
    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    //Actualizează statusul unei programări
    @Override
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}