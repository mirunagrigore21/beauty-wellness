package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

//Interfața care gestionează operațiile cu baza de date
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //Returnează programările unui client
    List<Appointment> findByClientId(Long clientId);
    //Returnează programările unui angajat
    List<Appointment> findByEmployeeId(Long employeeId);
    //Returnează programările cu un anumit status
    List<Appointment> findByStatus(AppointmentStatus status);
    //Returnează programările dintr-un interval de timp
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    //Returnează programările unui client cu un anumit status
    List<Appointment> findByClientIdAndStatus(Long clientId, AppointmentStatus status);
    //Returnează programările unui angajat cu un anumit status
    List<Appointment> findByEmployeeIdAndStatus(Long employeeId, AppointmentStatus status);
    //Returnează programările unui angajat dintr-un interval de timp
    List<Appointment> findByEmployeeIdAndAppointmentDateTimeBetween(Long employeeId, LocalDateTime start, LocalDateTime end);
    //Verifică dacă există programări ale angajatului în intervalul de timp dat
    List<Appointment> findByEmployeeIdAndAppointmentDateTimeBetweenAndStatusNot(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentStatus status
    );

    //Verifică suprapunerea reală de intervale pentru un angajat si xclude programările anulate si neprezentările
    @Query("SELECT a FROM Appointment a WHERE a.employee.id = :employeeId " +
            "AND a.appointmentDateTime < :end " +
            "AND a.appointmentDateTime > :start " +
            "AND a.status NOT IN :excludedStatuses")
    List<Appointment> findOverlappingAppointments(
            @Param("employeeId") Long employeeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("excludedStatuses") List<AppointmentStatus> excludedStatuses
    );
}