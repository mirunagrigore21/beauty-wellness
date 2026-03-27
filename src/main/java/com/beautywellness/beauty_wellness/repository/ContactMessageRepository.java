package com.beautywellness.beauty_wellness.repository;

import com.beautywellness.beauty_wellness.model.ContactMessage;
import com.beautywellness.beauty_wellness.model.ContactMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//repository pentru operatiile CRUD asupra mesajelor de contact
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    //returneaza mesajele dupa status
    List<ContactMessage> findByStatus(ContactMessageStatus status);
}