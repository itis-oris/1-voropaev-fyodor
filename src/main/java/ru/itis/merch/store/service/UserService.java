package ru.itis.merch.store.service;

import ru.itis.merch.store.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsersByStatus(String status);

    void changeUserStatus(String json);
}
