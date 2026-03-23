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

    //statistici
    //returneaza numarul de programari dintr-o luna
    @Query("SELECT COUNT(a) FROM Appointment a WHERE MONTH(a.appointmentDateTime) = :month AND YEAR(a.appointmentDateTime) = :year")
    Long countAppointmentsByMonth(@Param("month") int month, @Param("year") int year);

    //returneaza veniturile totale dintr-o luna
    @Query("SELECT SUM(a.service.price) FROM Appointment a WHERE MONTH(a.appointmentDateTime) = :month AND YEAR(a.appointmentDateTime) = :year AND a.status = 'COMPLETED'")
    Double getTotalRevenueByMonth(@Param("month") int month, @Param("year") int year);

    //returneaza totalul reducerilor dintr-o luna
    @Query("SELECT COALESCE(SUM(a.discount), 0) FROM Appointment a " +
            "WHERE MONTH(a.appointmentDateTime) = :month " +
            "AND YEAR(a.appointmentDateTime) = :year " +
            "AND a.status = 'COMPLETED'")
    Double getTotalDiscountsByMonth(@Param("month") int month, @Param("year") int year);
    //returneaza numarul de programari pe zi dintr-o luna
    @Query("SELECT DAY(a.appointmentDateTime), COUNT(a) FROM Appointment a WHERE MONTH(a.appointmentDateTime) = :month AND YEAR(a.appointmentDateTime) = :year GROUP BY DAY(a.appointmentDateTime)")
    List<Object[]> countAppointmentsByDayOfMonth(@Param("month") int month, @Param("year") int year);

    //returneaza cel mai popular serviciu
    @Query("SELECT a.service.name, COUNT(a) as total FROM Appointment a GROUP BY a.service.name ORDER BY total DESC")
    List<Object[]> getMostPopularServices();

    //returneaza angajatul cu cele mai multe programari
    @Query("SELECT a.employee.firstName, a.employee.lastName, COUNT(a) as total FROM Appointment a GROUP BY a.employee.id ORDER BY total DESC")
    List<Object[]> getTopEmployees();

    //returneaza rata de no-show
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = 'NO_SHOW' AND MONTH(a.appointmentDateTime) = :month AND YEAR(a.appointmentDateTime) = :year")
    Long countNoShowsByMonth(@Param("month") int month, @Param("year") int year);

    //returneaza venitul mediu per programare
    @Query("SELECT AVG(a.service.price) FROM Appointment a WHERE a.status = 'COMPLETED' AND MONTH(a.appointmentDateTime) = :month AND YEAR(a.appointmentDateTime) = :year")
    Double getAverageRevenuePerAppointment(@Param("month") int month, @Param("year") int year);
}