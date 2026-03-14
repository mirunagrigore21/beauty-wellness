package com.beautywellness.beauty_wellness.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

//clasa care reprezinta un utilizator
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    //identificatorul unic al utilizatorului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //numele complet
    private String name;
    //emailul
    @Column(unique = true)
    private String email;
    //parola criptata
    private String password;
    //rolul utilizatorului
    @Enumerated(EnumType.STRING)
    private Role role;
    //returneaza rolurile-springSecurity
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    //returneaza emailul ca username pentru autentificare
    @Override
    public String getUsername() {
        return email;
    }
    //contul nu expira niciodata
    @Override
    public boolean isAccountNonExpired() { return true; }
    //contul nu este blocat
    @Override
    public boolean isAccountNonLocked() { return true; }
    //credentialele nu expira niciodata
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    //contul este intotdeauna activ
    @Override
    public boolean isEnabled() { return true; }
}