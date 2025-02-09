package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends CrudDAO<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySessionId(String sessionId);
    List<User> findUserByStatus(String status);
}
