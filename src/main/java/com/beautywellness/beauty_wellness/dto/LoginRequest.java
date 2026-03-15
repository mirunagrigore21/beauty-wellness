package com.beautywellness.beauty_wellness.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//datele trimise de utilizator la autentificare
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Emailul este obligatoriu")
    @Email(message = "Emailul nu este valid")
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    private String password;
    }
