package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.repository.AppointmentRepository;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//controller REST pentru statistici
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;

    //returneaza statisticile complete pentru o luna
    @GetMapping(value = "/{year}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getMonthlyStatistics(
            @PathVariable int year,
            @PathVariable int month) {

        Map<String, Object> stats = new HashMap<>();

        //bumarul total de programari
        Long totalAppointments = appointmentRepository.countAppointmentsByMonth(month, year);
        stats.put("totalProgramari", totalAppointments);

        //veniturile totale
        Double totalRevenue = appointmentRepository.getTotalRevenueByMonth(month, year);
        stats.put("venituriTotale", totalRevenue != null ? totalRevenue : 0.0);

        //venitul mediu per programare
        Double avgRevenue = appointmentRepository.getAverageRevenuePerAppointment(month, year);
        stats.put("venitMediuPerProgramare", avgRevenue != null ? avgRevenue : 0.0);

        //numarul de no-show-uri
        Long noShows = appointmentRepository.countNoShowsByMonth(month, year);
        stats.put("numarNoShow", noShows);

        //rata no-show
        if (totalAppointments != null && totalAppointments > 0 && noShows != null) {
            double noShowRate = (noShows.doubleValue() / totalAppointments.doubleValue()) * 100;
            stats.put("rataNoShow", Math.round(noShowRate * 100.0) / 100.0 + "%");
        } else {
            stats.put("rataNoShow", "0%");
        }

        //programari pe zi
        List<Object[]> byDay = appointmentRepository.countAppointmentsByDayOfMonth(month, year);
        Map<String, Long> programariPeZi = new HashMap<>();
        for (Object[] row : byDay) {
            programariPeZi.put("Ziua " + row[0], ((Number) row[1]).longValue());
        }
        stats.put("programariPeZi", programariPeZi);

        //cel mai popular serviciu
        List<Object[]> popularServices = appointmentRepository.getMostPopularServices();
        if (!popularServices.isEmpty()) {
            stats.put("celMaiPopularServiciu", popularServices.get(0)[0]);
        }

        //top angajati
        List<Object[]> topEmployees = appointmentRepository.getTopEmployees();
        if (!topEmployees.isEmpty()) {
            stats.put("angajatulCuCeleMaiMulteProgramari",
                    topEmployees.get(0)[0] + " " + topEmployees.get(0)[1]);
        }

        //clienti noi in luna respectiva
        Long newClients = clientRepository.countByRegisteredAtBetween(
                java.time.LocalDateTime.of(year, month, 1, 0, 0),
                java.time.LocalDateTime.of(year, month, 1, 0, 0).plusMonths(1)
        );
        stats.put("clientiNoi", newClients);

        return ResponseEntity.ok(stats);
    }
}