package com.beautywellness.beauty_wellness;

import com.beautywellness.beauty_wellness.model.*;
import com.beautywellness.beauty_wellness.repository.AppointmentRepository;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//teste pentru serviciul de programari
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    //test pentru confirmarea unei programari
    @Test
    void testConfirmAppointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.PENDING);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment result = appointmentService.confirmAppointment(1L);

        assertEquals(AppointmentStatus.CONFIRMED, result.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    //test pentru anularea unei programari de catre client
    @Test
    void testCancelByClient() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment result = appointmentService.cancelByClient(1L);

        assertEquals(AppointmentStatus.CANCELLED_BY_CLIENT, result.getStatus());
    }

    //test pentru marcarea unei programari ca no-show
    @Test
    void testMarkNoShow() {
        Client client = new Client();
        client.setId(1L);
        client.setNoShowScore(0);
        client.setBlocked(false);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setClient(client);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(clientRepository.save(any())).thenReturn(client);

        Appointment result = appointmentService.markNoShow(1L);

        assertEquals(AppointmentStatus.NO_SHOW, result.getStatus());
        assertEquals(1, client.getNoShowScore());
        assertFalse(client.getBlocked());
    }

    //test pentru blocarea clientului dupa 3 no-show-uri
    @Test
    void testMarkNoShowBlocksClientAfter3() {
        Client client = new Client();
        client.setId(1L);
        client.setNoShowScore(2);
        client.setBlocked(false);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setClient(client);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(clientRepository.save(any())).thenReturn(client);

        appointmentService.markNoShow(1L);

        assertEquals(3, client.getNoShowScore());
        assertTrue(client.getBlocked());
    }

    //test pentru finalizarea unei programari si adaugarea punctelor de loialitate
    @Test
    void testCompleteAppointmentAddsLoyaltyPoints() {
        Client client = new Client();
        client.setId(1L);
        client.setLoyaltyPoints(0);
        client.setHasCoupon(false);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setClient(client);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(clientRepository.save(any())).thenReturn(client);

        appointmentService.completeAppointment(1L);

        assertEquals(1, client.getLoyaltyPoints());
        assertFalse(client.getHasCoupon());
    }

    //test pentru generarea cuponului dupa 5 programari finalizate
    @Test
    void testCompleteAppointmentGeneratesCouponAfter5Points() {
        Client client = new Client();
        client.setId(1L);
        client.setLoyaltyPoints(4);
        client.setHasCoupon(false);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setClient(client);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(clientRepository.save(any())).thenReturn(client);

        appointmentService.completeAppointment(1L);

        assertTrue(client.getHasCoupon());
        assertEquals(0, client.getLoyaltyPoints());
        assertNotNull(client.getCouponCode());
    }

    //test pentru programare negasita
    @Test
    void testConfirmAppointmentNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            appointmentService.confirmAppointment(99L);
        });
    }
}
