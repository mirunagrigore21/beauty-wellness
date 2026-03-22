package com.beautywellness.beauty_wellness.mapper;

import com.beautywellness.beauty_wellness.dto.SalonProcedureRequestDTO;
import com.beautywellness.beauty_wellness.dto.SalonProcedureResponseDTO;
import com.beautywellness.beauty_wellness.model.SalonProcedure;
import org.springframework.stereotype.Component;

//clasa care converteste intre SalonProcedure si DTO
@Component
public class SalonProcedureMapper {

    //converteste RequestDTO - SalonProcedure
    public SalonProcedure toEntity(SalonProcedureRequestDTO dto) {
        SalonProcedure procedure = new SalonProcedure();
        procedure.setName(dto.getName());
        procedure.setDescription(dto.getDescription());
        procedure.setDurationMinutes(dto.getDurationMinutes());
        procedure.setPrice(dto.getPrice());
        procedure.setCategory(dto.getCategory());
        return procedure;
    }

    //converteste SalonProcedure - ResponseDTO
    public SalonProcedureResponseDTO toResponseDTO(SalonProcedure procedure) {
        return SalonProcedureResponseDTO.builder()
                .id(procedure.getId())
                .name(procedure.getName())
                .description(procedure.getDescription())
                .durationMinutes(procedure.getDurationMinutes())
                .price(procedure.getPrice())
                .category(procedure.getCategory())
                .available(procedure.getAvailable())
                .build();
    }
}