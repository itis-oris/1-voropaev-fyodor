package ru.itis.merch.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.itis.merch.store.dao.UserDAO;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.exception.EntityNotFoundException;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final ObjectMapper mapper;

    @Override
    public List<User> getUsersByStatus(String status) {
        return userDAO.findUserByStatus(status);
    }

    @Override
    public void changeUserStatus(String json) {
        try {
            Long userId = mapper.readTree(json).findValue("userId").asLong();
            String status = mapper.readTree(json).findValue("status").asText();
            User user = userDAO.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            if ("Отклонён".equals(status)) {
                user.setStatus(status);
                user.setActivityPoints(0L);
                userDAO.updateById(userId, user);
                return;
            }
            if ("Подтверждён".equals(status)) {
                user.setStatus(status);
                Long activityPoints = mapper.readTree(json).findValue("points").asLong();
                user.setActivityPoints(activityPoints);
                userDAO.updateById(userId, user);
                return;
            }
            if ("Не подтверждён".equals(status)) {
                user.setStatus(status);
                userDAO.updateById(userId, user);
                return;
            }
            throw new IllegalStateException("Unknown status");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
