package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

//clasa care reprezinta programul de lucru al unui angajat
@Data
@Entity
@Table(name = "work_schedules")
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //angajatul caruia ii apartine programul
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    //ziua saptamanii
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    //ora de incepere
    @Column(nullable = false)
    private LocalTime startTime;

    //ora de terminare
    @Column(nullable = false)
    private LocalTime endTime;

    //daca angajatul lucreaza in acea zi
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isWorking = true;
}