package com.beautywellness.beauty_wellness;

import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//teste pentru serviciul de clienti
@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    //test pentru salvarea unui client nou
    @Test
    void testSaveClient() {
        Client client = new Client();
        client.setFirstName("Maria");
        client.setLastName("Popescu");
        client.setEmail("maria@test.com");

        when(clientRepository.save(any())).thenReturn(client);

        Client result = clientService.save(client);

        assertNotNull(result);
        assertEquals("Maria", result.getFirstName());
        verify(clientRepository, times(1)).save(client);
    }

    //test pentru gasirea unui client dupa ID
    @Test
    void testGetClientById() {
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("Maria");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Maria", result.get().getFirstName());
    }

    //test pentru client negasit dupa ID
    @Test
    void testGetClientByIdNotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.getById(99L);

        assertFalse(result.isPresent());
    }

    //test pentru gasirea unui client dupa email
    @Test
    void testGetClientByEmail() {
        Client client = new Client();
        client.setEmail("maria@test.com");

        when(clientRepository.findByEmail("maria@test.com")).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientByEmail("maria@test.com");

        assertTrue(result.isPresent());
        assertEquals("maria@test.com", result.get().getEmail());
    }

    //test pentru returnarea clientilor blocati
    @Test
    void testGetBlockedClients() {
        Client client1 = new Client();
        client1.setBlocked(true);
        Client client2 = new Client();
        client2.setBlocked(true);

        when(clientRepository.findByBlockedTrue()).thenReturn(List.of(client1, client2));

        List<Client> result = clientService.getBlockedClients();

        assertEquals(2, result.size());
        assertTrue(result.get(0).getBlocked());
    }

    //test pentru stergerea unui client
    @Test
    void testDeleteClient() {
        doNothing().when(clientRepository).deleteById(1L);

        assertDoesNotThrow(() -> clientService.delete(1L));
        verify(clientRepository, times(1)).deleteById(1L);
    }

    //test pentru eroare la stergere
    @Test
    void testDeleteClientThrowsException() {
        doThrow(new RuntimeException("Eroare")).when(clientRepository).deleteById(99L);

        assertThrows(RuntimeException.class, () -> clientService.delete(99L));
    }
}