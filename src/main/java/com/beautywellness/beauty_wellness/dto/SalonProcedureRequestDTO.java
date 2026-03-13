package com.beautywellness.beauty_wellness.dto;

import com.beautywellness.beauty_wellness.model.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

//DTO pentru primirea datelor unei proceduri de la frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalonProcedureRequestDTO {


    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private ServiceCategory category;
}