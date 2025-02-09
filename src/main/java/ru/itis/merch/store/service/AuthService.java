package ru.itis.merch.store.service;

import ru.itis.merch.store.entity.User;

public interface AuthService {

    void registerUser(String firstName, String lastName, String email, String password);
    User loginUser(String email, String password, String sessionId);
    void logoutUser(Long userId);
}
