package com.beautywellness.beauty_wellness.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//configurarea principala a securitatii aplicatiei
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                //configureaza CORS cu sursa noastra de configurare
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        //endpoint-urile publice
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/salon-procedures/**").permitAll()
                        .requestMatchers("/api/employees/by-category/**").permitAll()

                        //doar ADMIN poate inregistra angajati
                        .requestMatchers("/api/auth/register-employee").hasRole("ADMIN")

                        //clientii pot vedea angajatii filtrati dupa categorie pentru programare
                        .requestMatchers("/api/employees/by-category/**").hasAnyRole("ADMIN", "EMPLOYEE", "CLIENT")

                        //doar adminul poate gestiona angajatii
                        .requestMatchers("/api/employees/**").hasRole("ADMIN")

                        //clienti pot vedea propriile date
                        .requestMatchers("/api/clients/email/**").hasAnyRole("ADMIN", "EMPLOYEE", "CLIENT")

                        //ADMIN si EMPLOYEE pot vedea clientii
                        .requestMatchers("/api/clients/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        //ADMIN si EMPLOYEE pot gestiona procedurile
                        .requestMatchers("/api/salon-procedures/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        //toti autentificatii pot accesa programarile
                        .requestMatchers("/api/appointments/**").hasAnyRole("ADMIN", "EMPLOYEE", "CLIENT")

                        //ADMIN si EMPLOYEE pot gestiona programul de lucru
                        .requestMatchers("/api/work-schedules/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        //doar adminul poate vedea statisticile
                        .requestMatchers("/api/statistics/**").hasRole("ADMIN")

                        //orice alt request necesita autentificare
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        //nu folosim sesiuni
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                //adaugam filtrul JWT inainte de filtrul standard
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //verifica userul si parola din baza de date
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
