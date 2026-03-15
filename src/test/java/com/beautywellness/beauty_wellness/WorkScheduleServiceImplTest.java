package com.beautywellness.beauty_wellness;

import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.WorkSchedule;
import com.beautywellness.beauty_wellness.repository.WorkScheduleRepository;
import com.beautywellness.beauty_wellness.service.impl.WorkScheduleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//teste pentru serviciul de program de lucru
@ExtendWith(MockitoExtension.class)
public class WorkScheduleServiceImplTest {

    @Mock
    private WorkScheduleRepository workScheduleRepository;

    @InjectMocks
    private WorkScheduleServiceImpl workScheduleService;

    //test pentru salvarea unui program de lucru
    @Test
    void testSaveWorkSchedule() {
        Employee employee = new Employee();
        employee.setId(1L);

        WorkSchedule schedule = new WorkSchedule();
        schedule.setEmployee(employee);
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(19, 0));
        schedule.setIsWorking(true);

        when(workScheduleRepository.save(any())).thenReturn(schedule);

        WorkSchedule result = workScheduleService.saveWorkSchedule(schedule);

        assertNotNull(result);
        assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
        assertTrue(result.getIsWorking());
        verify(workScheduleRepository, times(1)).save(schedule);
    }

    //test pentru returnarea programului de lucru al unui angajat
    @Test
    void testGetWorkScheduleByEmployee() {
        WorkSchedule schedule1 = new WorkSchedule();
        schedule1.setDayOfWeek(DayOfWeek.MONDAY);
        WorkSchedule schedule2 = new WorkSchedule();
        schedule2.setDayOfWeek(DayOfWeek.TUESDAY);

        when(workScheduleRepository.findByEmployeeId(1L)).thenReturn(List.of(schedule1, schedule2));

        List<WorkSchedule> result = workScheduleService.getWorkScheduleByEmployee(1L);

        assertEquals(2, result.size());
        verify(workScheduleRepository, times(1)).findByEmployeeId(1L);
    }

    //test pentru returnarea zilelor lucratoare
    @Test
    void testGetWorkingDays() {
        WorkSchedule schedule = new WorkSchedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setIsWorking(true);

        when(workScheduleRepository.findByEmployeeIdAndIsWorking(1L, true))
                .thenReturn(List.of(schedule));

        List<WorkSchedule> result = workScheduleService.getWorkingDays(1L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsWorking());
    }

    //test pentru marcarea unei zile ca zi libera
    @Test
    void testMarkDayOff() {
        WorkSchedule schedule = new WorkSchedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setIsWorking(true);

        when(workScheduleRepository.findByEmployeeIdAndDayOfWeek(1L, DayOfWeek.MONDAY))
                .thenReturn(Optional.of(schedule));
        when(workScheduleRepository.save(any())).thenReturn(schedule);

        WorkSchedule result = workScheduleService.markDayOff(1L, DayOfWeek.MONDAY);

        assertFalse(result.getIsWorking());
        verify(workScheduleRepository, times(1)).save(schedule);
    }

    //test pentru marcarea unei zile ca zi lucratoare
    @Test
    void testMarkDayWorking() {
        WorkSchedule schedule = new WorkSchedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setIsWorking(false);

        when(workScheduleRepository.findByEmployeeIdAndDayOfWeek(1L, DayOfWeek.MONDAY))
                .thenReturn(Optional.of(schedule));
        when(workScheduleRepository.save(any())).thenReturn(schedule);

        WorkSchedule result = workScheduleService.markDayWorking(1L, DayOfWeek.MONDAY);

        assertTrue(result.getIsWorking());
    }

    //test pentru program de lucru negasit
    @Test
    void testMarkDayOffNotFound() {
        when(workScheduleRepository.findByEmployeeIdAndDayOfWeek(99L, DayOfWeek.MONDAY))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                workScheduleService.markDayOff(99L, DayOfWeek.MONDAY));
    }
}
