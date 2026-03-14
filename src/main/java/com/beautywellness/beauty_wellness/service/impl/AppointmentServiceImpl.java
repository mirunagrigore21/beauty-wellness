package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.*;
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

        //verifica daca angajatul poate efectua serviciul selectat
        if (!isEmployeeEligibleForService(
                appointment.getEmployee().getRole(),
                appointment.getService().getCategory())) {
            throw new RuntimeException("Angajatul " +
                    appointment.getEmployee().getFirstName() + " " +
                    appointment.getEmployee().getLastName() +
                    " nu poate efectua serviciul " +
                    appointment.getService().getName());
        }

        if (!isEmployeeAvailable(appointment.getEmployee().getId(), start, end)) {
            throw new RuntimeException("Angajatul nu este disponibil in intervalul selectat");
        }
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea programarii: " + e.getMessage());
        }
    }

    //returneaza toate programarile
    @Override
    public List<Appointment> getAllAppointments() {
        try {
            return appointmentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor: " + e.getMessage());
        }
    }

    //cauta o programare dupa ID
    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        try {
            return appointmentRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarii: " + e.getMessage());
        }
    }

    //actualizeaza datele unei programari existente si verifica daca angajatul este disponibil
    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));

        LocalDateTime start = appointment.getAppointmentDateTime();
        LocalDateTime end = start.plusMinutes(appointment.getService().getDurationMinutes());

        //verifica daca angajatul poate efectua serviciul selectat
        if (!isEmployeeEligibleForService(
                appointment.getEmployee().getRole(),
                appointment.getService().getCategory())) {
            throw new RuntimeException("Angajatul " +
                    appointment.getEmployee().getFirstName() + " " +
                    appointment.getEmployee().getLastName() +
                    " nu poate efectua serviciul " +
                    appointment.getService().getName());
        }

        if (!isEmployeeAvailable(appointment.getEmployee().getId(), start, end)) {
            throw new RuntimeException("Angajatul nu este disponibil in intervalul selectat");
        }

        existing.setClient(appointment.getClient());
        existing.setEmployee(appointment.getEmployee());
        existing.setService(appointment.getService());
        existing.setAppointmentDateTime(appointment.getAppointmentDateTime());
        existing.setNotes(appointment.getNotes());

        try {
            return appointmentRepository.save(existing);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea programarii: " + e.getMessage());
        }
    }

    //sterge o programare dupa ID
    @Override
    public void deleteAppointment(Long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la stergerea programarii: " + e.getMessage());
        }
    }

    //returneaza toate programarile unui client
    @Override
    public List<Appointment> getAppointmentsByClient(Long clientId) {
        try {
            return appointmentRepository.findByClientId(clientId);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor clientului: " + e.getMessage());
        }
    }

    //returneaza toate programarile unui angajat
    @Override
    public List<Appointment> getAppointmentsByEmployee(Long employeeId) {
        try {
            return appointmentRepository.findByEmployeeId(employeeId);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor angajatului: " + e.getMessage());
        }
    }

    //returneaza toate programarile de un anumit status
    @Override
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        try {
            return appointmentRepository.findByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor dupa status: " + e.getMessage());
        }
    }

    //returneaza toate programarile dintr-un interval de timp
    @Override
    public List<Appointment> getAppointmentsBetween(LocalDateTime start, LocalDateTime end) {
        try {
            return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor din interval: " + e.getMessage());
        }
    }

    //returneaza toate programarile unui angajat de un anumit status
    @Override
    public List<Appointment> getAppointmentsByEmployeeAndStatus(Long employeeId, AppointmentStatus status) {
        try {
            return appointmentRepository.findByEmployeeIdAndStatus(employeeId, status);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor angajatului dupa status: " + e.getMessage());
        }
    }

    //returneaza toate programarile unui client de un anumit status
    @Override
    public List<Appointment> getAppointmentsByClientAndStatus(Long clientId, AppointmentStatus status) {
        try {
            return appointmentRepository.findByClientIdAndStatus(clientId, status);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la obtinerea programarilor clientului dupa status: " + e.getMessage());
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
            throw new RuntimeException("Eroare la verificarea disponibilitatii angajatului: " + e.getMessage());
        }
    }

    //verifica daca angajatul poate efectua serviciul selectat
    private boolean isEmployeeEligibleForService(EmployeeRole employeeRole, ServiceCategory serviceCategory) {
        return switch (employeeRole) {
            case HAIR_STYLIST -> serviceCategory == ServiceCategory.HAIR;
            case MANICURIST -> serviceCategory == ServiceCategory.NAILS;
            case PEDICURIST -> serviceCategory == ServiceCategory.NAILS;
            case COSMETICIAN -> serviceCategory == ServiceCategory.SKIN_CARE;
            case MAKEUP_ARTIST -> serviceCategory == ServiceCategory.MAKEUP;
            case EYEBROW_SPECIALIST -> serviceCategory == ServiceCategory.EYEBROWS;
            case EYELASH_TECHNICIAN -> serviceCategory == ServiceCategory.EYELASHES;
            case MASSAGE_THERAPIST -> serviceCategory == ServiceCategory.MASSAGE;
            case SPA_THERAPIST -> serviceCategory == ServiceCategory.SPA;
            case AROMATHERAPIST -> serviceCategory == ServiceCategory.AROMATHERAPY;
            case REFLEXOLOGIST -> serviceCategory == ServiceCategory.REFLEXOLOGY;
            case RECEPTIONIST, MANAGER -> false;
        };
    }

    //confirma o programare
    @Override
    public Appointment confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la confirmarea programarii: " + e.getMessage());
        }
    }

    //anuleaza o programare de catre client
    @Override
    public Appointment cancelByClient(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_CLIENT);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la anularea programarii de catre client: " + e.getMessage());
        }
    }

    //anuleaza o programare de catre salon
    @Override
    public Appointment cancelBySalon(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));
        appointment.setStatus(AppointmentStatus.CANCELLED_BY_SALON);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la anularea programarii de catre salon: " + e.getMessage());
        }
    }

    //marcheaza o programare pentru neprezentare si creste scorul no-show
    @Override
    public Appointment markNoShow(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));
        appointment.setStatus(AppointmentStatus.NO_SHOW);

        Client client = appointment.getClient();
        int newScore = client.getNoShowScore() + 1;
        client.setNoShowScore(newScore);

        if (newScore >= 3) {
            //blocam clientul dupa 3 no-show-uri
            client.setBlocked(true);
            client.setBlockedReason("Cont blocat automat dupa 3 neprezentari");
        } else if (newScore == 2) {
            //avertisment dupa 2 no-show-uri
            client.setWarningMessage("Atentie! Ai acumulat 2 neprezentari. La urmatoarea neprezentare contul tau va fi blocat!");
        }

        try {
            clientRepository.save(client);
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la marcarea neprezentarii: " + e.getMessage());
        }
    }

    //marcheaza o programare ca finalizata si adauga puncte de loialitate
    @Override
    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));
        appointment.setStatus(AppointmentStatus.COMPLETED);

        //adauga punct de loialitate clientului
        Client client = appointment.getClient();
        int newPoints = client.getLoyaltyPoints() + 1;
        client.setLoyaltyPoints(newPoints);

        //verifica daca clientul a acumulat 5 puncte ofera cupon si reseteaza
        if (newPoints >= 5) {
            client.setHasCoupon(true);
            client.setCouponCode("BW-" + client.getId() + "-" + System.currentTimeMillis());
            client.setLoyaltyPoints(0);
        }

        try {
            clientRepository.save(client);
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la finalizarea programarii: " + e.getMessage());
        }
    }

    //actualizeaza statusul unei programari
    @Override
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programarea nu a fost gasita"));
        appointment.setStatus(status);
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea statusului programarii: " + e.getMessage());
        }
    }
}