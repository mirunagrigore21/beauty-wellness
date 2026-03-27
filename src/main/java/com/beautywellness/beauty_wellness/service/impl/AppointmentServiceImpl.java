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

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        LocalDateTime start = appointment.getAppointmentDateTime();
        LocalDateTime end = start.plusMinutes(appointment.getService().getDurationMinutes());
        if (start.getDayOfWeek().getValue() == 7) {
            throw new RuntimeException("Salonul este inchis duminica!");
        }

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
        //verifica daca clientul este blocat
        if (Boolean.TRUE.equals(appointment.getClient().getBlocked())) {
            throw new RuntimeException("Contul tău este blocat din cauza neprezentărilor repetate. Contactează salonul pentru deblocare!");
        }
        //verifica daca clientul nu are deja o programare in acelasi interval
        List<AppointmentStatus> excludedStatuses = List.of(
                AppointmentStatus.CANCELLED_BY_CLIENT,
                AppointmentStatus.CANCELLED_BY_SALON,
                AppointmentStatus.NO_SHOW
        );

        List<Appointment> conflicteClient = appointmentRepository
                .findByClientId(appointment.getClient().getId())
                .stream()
                .filter(a -> !excludedStatuses.contains(a.getStatus()))
                .filter(a -> {
                    LocalDateTime aStart = a.getAppointmentDateTime();
                    LocalDateTime aEnd = aStart.plusMinutes(a.getService().getDurationMinutes());
                    return start.isBefore(aEnd) && end.isAfter(aStart);
                })
                .collect(java.util.stream.Collectors.toList());

        if (!conflicteClient.isEmpty()) {
            throw new RuntimeException("Ai deja o programare in acest interval de timp!");
        }

        //verifica si valideaza cuponul daca exista in note
        Client client = appointment.getClient();
        appointment.setDiscount(0.0);

        if (appointment.getNotes() != null && appointment.getNotes().contains("CUPON:")) {
            String notes = appointment.getNotes();

            //extrage codul cuponului din note
            String codCupon = notes.substring(notes.indexOf("CUPON:") + 6).trim();

            //elimina textul (-20 RON) daca exista
            if (codCupon.contains("(-20 RON)")) {
                codCupon = codCupon.replace("(-20 RON)", "").trim();
            }

            //elimina orice note suplimentare dupa |
            if (codCupon.contains("|")) {
                codCupon = codCupon.substring(0, codCupon.indexOf("|")).trim();
            }

            //valideaza ca clientul are cuponul si codul e corect
            if (Boolean.TRUE.equals(client.getHasCoupon())
                    && client.getCouponCode() != null
                    && client.getCouponCode().equals(codCupon)) {

                //aplica reducerea si consuma cuponul
                appointment.setDiscount(20.0);
                client.setHasCoupon(false);
                client.setCouponCode(null);
                clientRepository.save(client);
            } else {
                throw new RuntimeException("Cuponul este invalid sau nu mai este disponibil!");
            }
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
        if (start.getDayOfWeek().getValue() == 7) {
            throw new RuntimeException("Salonul este inchis duminica!");
        }

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
            client.setBlocked(true);
            client.setBlockedReason("Cont blocat automat dupa 3 neprezentari");
            client.setWarningMessage(null);


            // anulam automat toate programarile viitoare active ale clientului
            List<Appointment> programariViitoare = appointmentRepository
                    .findByClientId(client.getId())
                    .stream()
                    .filter(a -> a.getAppointmentDateTime().isAfter(LocalDateTime.now()))
                    .filter(a -> a.getStatus() == AppointmentStatus.PENDING
                            || a.getStatus() == AppointmentStatus.CONFIRMED)
                    .collect(java.util.stream.Collectors.toList());

            for (Appointment a : programariViitoare) {
                a.setStatus(AppointmentStatus.CANCELLED_BY_SALON);
            }

            appointmentRepository.saveAll(programariViitoare);

        } else if (newScore == 2) {
            // avertisment dupa 2 no-show-uri
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