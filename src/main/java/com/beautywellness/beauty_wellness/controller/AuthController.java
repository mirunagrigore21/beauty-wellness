package com.beautywellness.beauty_wellness.controller;

import com.beautywellness.beauty_wellness.config.JwtService;
import com.beautywellness.beauty_wellness.dto.AuthResponse;
import com.beautywellness.beauty_wellness.dto.LoginRequest;
import com.beautywellness.beauty_wellness.dto.RegisterEmployeeRequest;
import com.beautywellness.beauty_wellness.dto.RegisterRequest;
import com.beautywellness.beauty_wellness.model.Client;
import com.beautywellness.beauty_wellness.model.Employee;
import com.beautywellness.beauty_wellness.model.Role;
import com.beautywellness.beauty_wellness.model.User;
import com.beautywellness.beauty_wellness.repository.ClientRepository;
import com.beautywellness.beauty_wellness.repository.EmployeeRepository;
import com.beautywellness.beauty_wellness.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

//controller pentru inregistrare si autentificare
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //inregistrare client (POST /api/auth/register)
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        //creeaza user pentru autentificare
        User user = User.builder()
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();
        userRepository.save(user);

        //creeaza contul client cu datele complete
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setBirthDate(request.getBirthDate());
        clientRepository.save(client);

        //genereaza token JWT
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getDisplayName())
                .build());
    }

    //inregistrare angajat (doar ADMIN - POST /api/auth/register-employee)
    @PostMapping(value = "/register-employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> registerEmployee(@Valid @RequestBody RegisterEmployeeRequest request) {

        //creeaza user pentru autentificare
        User user = User.builder()
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .build();
        userRepository.save(user);

        //creeaza angajatul cu datele complete
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setRole(request.getEmployeeRole());
        employeeRepository.save(employee);

        //genereaza token JWT
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getDisplayName())
                .build());
    }

    //autentificare — POST /api/auth/login
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        //verifica emailul si parola
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        //gaseste userul
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        //genereaza token JWT
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getDisplayName())
                .build());
    }
}