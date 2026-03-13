package com.beautywellness.beauty_wellness.service;

import java.util.List;
import java.util.Optional;

//interfața generica pentru operatiile comune CRUD
public interface BaseService<T, ID> {
    T save(T entity);
    List<T> getAll();
    Optional<T> getById(ID id);
    T update(ID id, T entity);
    void delete(ID id);
}