package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Interfață pentru accesul la baza de date pentru entitatea User
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Găsește un utilizator după email
    Optional<User> findByEmail(String email);
}