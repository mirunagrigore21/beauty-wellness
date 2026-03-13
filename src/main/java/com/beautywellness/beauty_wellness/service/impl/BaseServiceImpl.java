package com.beautywellness.beauty_wellness.service.impl;

import com.beautywellness.beauty_wellness.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

//implementare generica pentru operatiile comune CRUD
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    //returneaza repository-ul specific fiecarei entități
    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    public List<T> getAll() {
        return getRepository().findAll();
    }

    //cauta dupa ID
    @Override
    public Optional<T> getById(ID id) {
        return getRepository().findById(id);
    }

    //sterge după ID
    @Override
    public void delete(ID id) {
        try {
            getRepository().deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la ștergere: " + e.getMessage());
        }
    }
}