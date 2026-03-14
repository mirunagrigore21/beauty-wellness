package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.model.WorkSchedule;
import com.beautywellness.beauty_wellness.repository.WorkScheduleRepository;
import com.beautywellness.beauty_wellness.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

// Implementarea serviciului pentru programul de lucru
@Service
@RequiredArgsConstructor
public class WorkScheduleServiceImpl implements WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;

    //adauga un program de lucru pentru un angajat
    @Override
    public WorkSchedule saveWorkSchedule(WorkSchedule workSchedule) {
        try {
            return workScheduleRepository.save(workSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la salvarea programului de lucru: " + e.getMessage());
        }
    }

    //returneaza programul de lucru al unui angajat
    @Override
    public List<WorkSchedule> getWorkScheduleByEmployee(Long employeeId) {
        return workScheduleRepository.findByEmployeeId(employeeId);
    }

    //returneaza programul de lucru al unui angajat pentru o zi specifica
    @Override
    public Optional<WorkSchedule> getWorkScheduleByEmployeeAndDay(Long employeeId, DayOfWeek dayOfWeek) {
        return workScheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId, dayOfWeek);
    }

    //returneaza zilele in care un angajat lucreaza
    @Override
    public List<WorkSchedule> getWorkingDays(Long employeeId) {
        return workScheduleRepository.findByEmployeeIdAndIsWorking(employeeId, true);
    }

    //actualizeaza programul de lucru
    @Override
    public WorkSchedule updateWorkSchedule(Long id, WorkSchedule workSchedule) {
        workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programul de lucru cu id-ul " + id + " nu a fost gasit"));
        workSchedule.setId(id);
        try {
            return workScheduleRepository.save(workSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la actualizarea programului de lucru: " + e.getMessage());
        }
    }

    //marcheaza o zi ca zi libera pentru un angajat
    @Override
    public WorkSchedule markDayOff(Long employeeId, DayOfWeek dayOfWeek) {
        WorkSchedule schedule = workScheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId, dayOfWeek)
                .orElseThrow(() -> new RuntimeException("Programul de lucru nu a fost gasit"));
        schedule.setIsWorking(false);
        return workScheduleRepository.save(schedule);
    }

    //marcheaza o zi ca zi lucratoare pentru un angajat
    @Override
    public WorkSchedule markDayWorking(Long employeeId, DayOfWeek dayOfWeek) {
        WorkSchedule schedule = workScheduleRepository.findByEmployeeIdAndDayOfWeek(employeeId, dayOfWeek)
                .orElseThrow(() -> new RuntimeException("Programul de lucru nu a fost gasit"));
        schedule.setIsWorking(true);
        return workScheduleRepository.save(schedule);
    }

    //sterge programul de lucru
    @Override
    public void deleteWorkSchedule(Long id) {
        workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programul de lucru cu id-ul " + id + " nu a fost gasit"));
        try {
            workScheduleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la stergerea programului de lucru: " + e.getMessage());
        }
    }
}