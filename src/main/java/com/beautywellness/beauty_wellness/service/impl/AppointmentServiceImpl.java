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

//clasa care implementeaza logica pentru gestionarea programarilor
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    //repository pentru operatiile CRUD asupra programarilor
    private final AppointmentRepository appointmentRepository;

    //repository pentru actualizarea datelor clientului (no-show, blocare)
    private final ClientRepository clientRepository;

    //salveaza o programare noua dupa ce verifica daca angajatul este disponibil
    @Override
    public Appointment saveAppointment(Appointment appointment) {
        LocalDateTime start = appointment.getAppointmentDateTime();
        LocalDateTime end = start.plusMinutes(appointment.getService().getDurationMinutes());
        if (!isEmployeeAvailable(appointment.getEmployee().getId(), start, end)) {
            throw new RuntimeException("Angajatul nu este disponibil în intervalul selectat");
        }
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea programării: " + e.getMessage());
        }
    }
    //returneaza toate programările
    @Override
    public List<Appointment> getAllAppointments() {
        try {
            return appointmentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor: " + e.getMessage());
        }
    }
    //cauta o programare dupa ID
    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        try {
            return appointmentRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programării: " + e.getMessage());
        }
    }
    //actualizeaza datele unei programări existente si verifica daca angajatul este disponibil pentru modificari
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

        try {
            return appointmentRepository.save(existing);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea programării: " + e.getMessage());
        }
    }

    //sterge o programare dupa ID
    @Override
    public void deleteAppointment(Long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la ștergerea programării: " + e.getMessage());
        }
    }

    //returneaza toate programarile pe care le are un client
    @Override
    public List<Appointment> getAppointmentsByClient(Long clientId) {
        try {
            return appointmentRepository.findByClientId(clientId);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor clientului: " + e.getMessage());
        }
    }

    //returneaza toate programarile pe care le are un angajat
    @Override
    public List<Appointment> getAppointmentsByEmployee(Long employeeId) {
        try {
            return appointmentRepository.findByEmployeeId(employeeId);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor angajatului: " + e.getMessage());
        }
    }

    //returneaza toate programarile de un anumit tip
    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        try {
            return appointmentRepository.findByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor după status: " + e.getMessage());
        }
    }

    //returneaza toate programarile dintr-un interval de timp
    @Override
    public List<Appointment> getAppointmentsBetween(LocalDateTime start, LocalDateTime end) {
        try {
            return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor din interval: " + e.getMessage());
        }
    }

    //returneaza toate programarile unui angajat de un anumit tip
    @Override
    public List<Appointment> getAppointmentsByEmployeeAndStatus(Long employeeId, AppointmentStatus status) {
        try {
            return appointmentRepository.findByEmployeeIdAndStatus(employeeId, status);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor angajatului după status: " + e.getMessage());
        }
    }
    //returneaza toate programarile unui client de un anumit tip
    @Override
    public List<Appointment> getAppointmentsByClientAndStatus(Long clientId, AppointmentStatus status) {
        try {
            return appointmentRepository.findByClientIdAndStatus(clientId, status);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obținerea programărilor clientului după status: " + e.getMessage());
        }
    }

    //verifica daca angajatul este disponibil in intervalul de timp dat
    @Override
    public boolean isEmployeeAvailable(Long employeeId, LocalDateTime start, LocalDateTime end) {
        try {
            List<AppointmentStatus> excludedStatuses = List.of(
                    AppointmentStatus.CANCELLED_BY_CLIENT,
                    AppointmentStatus.CANCELLED_BY_SALON,
                    AppointmentStatus.NO_SHOW
            );
            List<Appointment> overlapping = appointmentRepository.findOverlappingAppointments(
                    employeeId, start, end, excludedStatuses
            );
            return overlapping.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la verificarea disponibilității angajatului: " + e.getMessage());
        }
    }

    //confirma o programare
    @Override
    public Appointment confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la confirmarea programării: " + e.getMessage());
        }
    }

    //anuleaza o programare de catre client
    @Override
    public Appointment cancelByClient(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_CLIENT);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la anularea programării de către client: " + e.getMessage());
        }
    }

    //anuleaza o programare de catre salon
    @Override
    public Appointment cancelBySalon(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_SALON);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la anularea programării de către salon: " + e.getMessage());
        }
    }

    //marcheaza o programare pentru neprezentare si creste scorul no-show
    @Override
    public Appointment markNoShow(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.NO_SHOW);

        Client client = appointment.getClient();
        int newScore = client.getNoShowScore() + 1;
        client.setNoShowScore(newScore);

        if (newScore >= 3) {
            client.setBlocked(true);
        }

        try {
            clientRepository.save(client);
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la marcarea neprezentării: " + e.getMessage());
        }
    }

    //marcheaza o programare ca finalizata
    @Override
    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la finalizarea programării: " + e.getMessage());
        }
    }

    //actualizeaza statusul unei programari
    @Override
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost găsită"));
        appointment.setStatus(status);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea statusului programării: " + e.getMessage());
        }
    }
}