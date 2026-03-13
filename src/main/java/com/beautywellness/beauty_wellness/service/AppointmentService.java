package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Interfața care definește operațiile disponibile pentru gestionarea programărilor
public interface AppointmentService {

    //Salvează o programare nouă în baza
    Appointment saveAppointment(Appointment appointment);
    //Returnează toate programările din baza de
    List<Appointment> getAllAppointments();
    //Caută o programare după ID
    Optional<Appointment> getAppointmentById(Long id);
    //Actualizează o programare existentă
    Appointment updateAppointment(Long id, Appointment appointment);
    //Șterge o programare după ID
    void deleteAppointment(Long id);
    //Returnează toate programările unui client
    List<Appointment> getAppointmentsByClient(Long clientId);
    //Returnează toate programările unui angajat
    List<Appointment> getAppointmentsByEmployee(Long employeeId);
    //Returnează toate programările cu un anumit status
    List<Appointment> getAppointmentsByStatus(AppointmentStatus status);
    //Returnează toate programările dintr-un interval de timp
    List<Appointment> getAppointmentsBetween(LocalDateTime start, LocalDateTime end);
    //Returnează toate programările unui angajat cu un anumit status
    List<Appointment> getAppointmentsByEmployeeAndStatus(Long employeeId, AppointmentStatus status);
    //Returnează toate programările unui client cu un anumit status
    List<Appointment> getAppointmentsByClientAndStatus(Long clientId, AppointmentStatus status);
    //Actualizează statusul unei programări
    Appointment updateAppointmentStatus(Long id, AppointmentStatus status);
    //Verifică dacă angajatul este disponibil în intervalul de timp dat
    boolean isEmployeeAvailable(Long employeeId, LocalDateTime start, LocalDateTime end);
    //Confirmă o programare
    Appointment confirmAppointment(Long id);
    //Anulează o programare de către client
    Appointment cancelByClient(Long id);
    //Anulează o programare de către salon
    Appointment cancelBySalon(Long id);
    //Marchează o programare ca neprezentare-creste automat scorul no-show al clientului si il blochează dacă e necesar
    Appointment markNoShow(Long id);
    //Marchează o programare ca finalizată
    Appointment completeAppointment(Long id);
}