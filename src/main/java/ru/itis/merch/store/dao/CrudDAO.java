package ru.itis.merch.store.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, ID> {

    void save(T object);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void updateById(ID id, T object);

}
