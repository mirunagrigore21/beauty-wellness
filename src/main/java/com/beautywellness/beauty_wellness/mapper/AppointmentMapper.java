package com.beautywellness.beauty_wellness.mapper;

import com.beautywellness.beauty_wellness.dto.AppointmentRequestDTO;
import com.beautywellness.beauty_wellness.dto.AppointmentResponseDTO;
import com.beautywellness.beauty_wellness.model.Appointment;
import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.SalonProcedure;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.repository.EmployeeRepository;
import com.beautywellness.beauty_wellness.repository.SalonProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//clasa care converteste intre Appointment si DTO
@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final SalonProcedureRepository salonProcedureRepository;
    private final ClientMapper clientMapper;
    private final EmployeeMapper employeeMapper;
    private final SalonProcedureMapper salonProcedureMapper;


    //converteste RequestDTO - Appointment
    public Appointment toEntity(AppointmentRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Clientul nu a fost găsit"));
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost găsit"));
        SalonProcedure service = salonProcedureRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Procedura nu a fost găsită"));

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setService(service);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setNotes(dto.getNotes());
        return appointment;
    }

    //converteste Appointment-ResponseDTO
    public AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .client(clientMapper.toResponseDTO(appointment.getClient()))
                .employee(employeeMapper.toResponseDTO(appointment.getEmployee()))
                .service(salonProcedureMapper.toResponseDTO(appointment.getService()))
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .discount(appointment.getDiscount())
                .build();
    }
}