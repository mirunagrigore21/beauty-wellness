package com.beautywellness.beauty_wellness.service;

import com.beautywellness.beauty_wellness.model.WorkSchedule;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

//interfata pentru serviciul de program de lucru
public interface WorkScheduleService {

    WorkSchedule saveWorkSchedule(WorkSchedule workSchedule);

    List<WorkSchedule> getWorkScheduleByEmployee(Long employeeId);

    Optional<WorkSchedule> getWorkScheduleByEmployeeAndDay(Long employeeId, DayOfWeek dayOfWeek);

    List<WorkSchedule> getWorkingDays(Long employeeId);

    WorkSchedule updateWorkSchedule(Long id, WorkSchedule workSchedule);

    WorkSchedule markDayOff(Long employeeId, DayOfWeek dayOfWeek);

    WorkSchedule markDayWorking(Long employeeId, DayOfWeek dayOfWeek);

    void deleteWorkSchedule(Long id);
}