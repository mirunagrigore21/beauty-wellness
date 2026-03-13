package com.beautywellness.beauty_wellness.mapper;

import com.beautywellness.beauty_wellness.dto.ClientRequestDTO;
import com.beautywellness.beauty_wellness.dto.ClientResponseDTO;
import com.beautywellness.beauty_wellness.model.Client;
import org.springframework.stereotype.Component;

//clasa care convertește intre Client si DTO
@Component
public class ClientMapper {

    //converteste RequestDTO - Client
    public Client toEntity(ClientRequestDTO dto) {
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setBirthDate(dto.getBirthDate());
        client.setNotes(dto.getNotes());
        return client;
    }

    //converteste Client - ResponseDTO
    public ClientResponseDTO toResponseDTO(Client client) {
        return ClientResponseDTO.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .birthDate(client.getBirthDate())
                .notes(client.getNotes())
                .build();
    }
}