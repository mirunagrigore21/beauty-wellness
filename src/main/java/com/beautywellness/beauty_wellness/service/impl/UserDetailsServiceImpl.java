package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//implementarea serviciului care incarca detaliile utilizatorului din baza de date
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    //caută utilizatorul după email — folosit de Spring Security la autentificare
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilizatorul cu emailul " + email + " nu a fost găsit"
                ));
    }
}
   