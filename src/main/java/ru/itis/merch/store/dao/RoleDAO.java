package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.Role;

import java.util.Optional;

public interface RoleDAO extends CrudDAO<Role, Long> {

    Optional<Role> findByName(String name);
}
