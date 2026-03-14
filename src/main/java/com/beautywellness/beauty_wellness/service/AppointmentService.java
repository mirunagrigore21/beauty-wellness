package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//interfata care defineste operatiile disponibile pentru gestionarea programarilor
public interface AppointmentService {

    //salveaza o programare noua în baza
    Appointment saveAppointment(Appointment appointment);
    //returneaza toate programarile
    List<Appointment> getAllAppointments();
    //cauta o programare după ID
    Optional<Appointment> getAppointmentById(Long id);
    //actualizeaza o programare existenta
    Appointment updateAppointment(Long id, Appointment appointment);
    //sterge o programare după ID
    void deleteAppointment(Long id);
    //returneaza toate programarile unui client
    List<Appointment> getAppointmentsByClient(Long clientId);
    //returneaza toate programarile unui angajat
    List<Appointment> getAppointmentsByEmployee(Long employeeId);
    //returneaza toate programarile cu un anumit status
    List<Appointment> getAppointmentsByStatus(AppointmentStatus status);
    //returneaza toate programarile dintr-un interval de timp
    List<Appointment> getAppointmentsBetween(LocalDateTime start, LocalDateTime end);
    //returneaza toate programarile unui angajat cu un anumit status
    List<Appointment> getAppointmentsByEmployeeAndStatus(Long employeeId, AppointmentStatus status);
    //returnează toate programarile unui client cu un anumit status
    List<Appointment> getAppointmentsByClientAndStatus(Long clientId, AppointmentStatus status);
    //cctualizeaza statusul unei programari
    Appointment updateAppointmentStatus(Long id, AppointmentStatus status);
    //verifica daca angajatul este disponibil in intervalul de timp dat
    boolean isEmployeeAvailable(Long employeeId, LocalDateTime start, LocalDateTime end);
    //confirma o programare
    Appointment confirmAppointment(Long id);
    //anuleaza o programare de catre client
    Appointment cancelByClient(Long id);
    //anuleaza o programare de catre salon
    Appointment cancelBySalon(Long id);
    //marchează o programare ca neprezentare
    Appointment markNoShow(Long id);
    //marcheaza o programare ca finalizata
    Appointment completeAppointment(Long id);
}