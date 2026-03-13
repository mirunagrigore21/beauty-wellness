package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

//Interfața care gestioneaza operatiile cu baza de date pentru programari
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //Returneaza programarile unui client
    List<Appointment> findByClientId(Long clientId);
    //Returneaza programarile unui angajat
    List<Appointment> findByEmployeeId(Long employeeId);
    //Returnează programările de un anumit tip
    List<Appointment> findByStatus(AppointmentStatus status);
    //Returneaza programarile dintr-un interval de timp
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    //Returnează programările  unui client de un anumit tip
    List<Appointment> findByClientIdAndStatus(Long clientId, AppointmentStatus status);
    //Returnează programările  unui angajat de un anumit tip
    List<Appointment> findByEmployeeIdAndStatus(Long employeeId, AppointmentStatus status);
    //Returneaza programarile unui angajat dintr-un interval de timp
    List<Appointment> findByEmployeeIdAndAppointmentDateTimeBetween(
                                                Long employeeId,
                                                LocalDateTime start,
                                                LocalDateTime end
    );
    //Returneaza programarile unui angajat dintr-un interval de timp dintr-o categorie
    List<Appointment> findByEmployeeIdAndAppointmentDateTimeBetweenAndStatusNot(
                                                Long employeeId,
                                                LocalDateTime start,
                                                LocalDateTime end,
                                                AppointmentStatus status
    );

    //Verifica suprapunerea reala intre intervalele unor programari si exclude programarile anulate si neprezentarile
    @Query("SELECT a FROM Appointment a WHERE a.employee.id = :employeeId " +
            "AND a.appointmentDateTime BETWEEN :start AND :end " +
            "AND a.status NOT IN :excludedStatuses")
    List<Appointment> findOverlappingAppointments(
            @Param("employeeId")       Long employeeId,
            @Param("start")            LocalDateTime start,
            @Param("end")              LocalDateTime end,
            @Param("excludedStatuses") List<AppointmentStatus> excludedStatuses
    );
}