package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

//DTO pentru trimiterea datelor unei proceduri catre frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalonProcedureResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private ServiceCategory category;
    private Boolean available;
}