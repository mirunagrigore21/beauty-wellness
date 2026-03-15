
package com.beautywellness.beauty_wellness.dto;

import jakarta.validation.constraints.*;
        import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

//datele trimise de client la inregistrare
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Prenumele este obligatoriu")
    private String firstName;

    @NotBlank(message = "Numele este obligatoriu")
    private String lastName;

    @NotBlank(message = "Emailul este obligatoriu")
    @Email(message = "Emailul nu este valid")
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    @Size(min = 6, message = "Parola trebuie sa aiba minim 6 caractere")
    private String password;

    @NotBlank(message = "Telefonul este obligatoriu")
    private String phone;

    @NotNull(message = "Data nasterii este obligatorie")
    @Past(message = "Data nasterii trebuie sa fie in trecut")
    private LocalDate birthDate;
}