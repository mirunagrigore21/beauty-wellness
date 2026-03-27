package com.beautywellness.beauty_wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

//DTO pentru trimiterea datelor unui client catre frontend
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String notes;
    private Integer noShowScore;
    private Boolean blocked;
    private String blockedReason;
    private String warningMessage;
    private Integer loyaltyPoints;
    private Boolean hasCoupon;
    private String couponCode;
    private LocalDateTime registeredAt;
}