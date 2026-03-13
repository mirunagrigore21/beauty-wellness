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

//Clasa care reprezintă un utilizator al aplicației-implementează UserDetails pentru integrarea cu Spring Security

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    //Identificatorul unic al utilizatorului
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Numele complet al utilizatorului
    private String name;
    //Emailul utilizatorului- folosit la autentificare
    @Column(unique = true)
    private String email;
    //Parola criptată a utilizatorului
    private String password;
    //Rolul utilizatorului în aplicație (admin sau client)
    @Enumerated(EnumType.STRING)
    private Role role;
    //Returnează rolurile utilizatorului pentru Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    //Returnează emailul ca username pentru autentificare
    @Override
    public String getUsername() {
        return email;
    }
    //Contul nu expiră niciodată
    @Override
    public boolean isAccountNonExpired() { return true; }
    //Contul nu este blocat
    @Override
    public boolean isAccountNonLocked() { return true; }
    //Credențialele nu expiră niciodată
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    //Contul este întotdeauna activ
    @Override
    public boolean isEnabled() { return true; }
}