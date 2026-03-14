package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

//repository pentru programul de lucru al angajatilor
@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    List<WorkSchedule> findByEmployeeId(Long employeeId);

    Optional<WorkSchedule> findByEmployeeIdAndDayOfWeek(Long employeeId, DayOfWeek dayOfWeek);

    List<WorkSchedule> findByEmployeeIdAndIsWorking(Long employeeId, Boolean isWorking);
}
