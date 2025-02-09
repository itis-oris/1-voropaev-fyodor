package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.UserDAO;
import ru.itis.merch.store.entity.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    // language=sql
    private final static String INSERT_USER = "INSERT INTO \"user\" (first_name, last_name, email, status, activity_points, password, session_id, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    // language=sql
    private final static String FIND_USER_BY_ID = "SELECT * FROM \"user\" WHERE id = ?";
    // language=sql
    private final static String FIND_ALL_USERS = "SELECT * FROM \"user\"";
    // language=sql
    private final static String DELETE_USER_BY_ID = "DELETE FROM \"user\" WHERE id = ?";
    // language=sql
    private final static String FIND_USER_BY_EMAIL = "SELECT * FROM \"user\" WHERE email = ?";
    // language=sql
    private final static String FIND_USER_BY_SESSION_ID = "SELECT * FROM \"user\" WHERE session_id = ?";
    // language=sql
    private final static String UPDATE_USER_BY_ID = "UPDATE \"user\" SET first_name = ?, last_name = ?, email = ?, password = ?, status = ?, session_id = ?, activity_points = ? WHERE id = ?";
    // language=sql
    private final static String FIND_BY_STATUS = "SELECT * FROM \"user\" WHERE status = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;


    @Override
    public void save(User object) {
        jdbcTemplate.update(INSERT_USER, object.getFirstName(), object.getLastName(), object.getEmail(), object.getStatus(), object.getActivityPoints(), object.getPassword(), null, object.getRoleId());
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_USER_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_USERS, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_USER_BY_ID, id);
    }

    @Override
    public void updateById(Long id, User object) {
        jdbcTemplate.update(UPDATE_USER_BY_ID, object.getFirstName(), object.getLastName(), object.getEmail(), object.getPassword(), object.getStatus(), object.getSessionId(), object.getActivityPoints(), id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, rowMapper, email));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findBySessionId(String sessionId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_USER_BY_SESSION_ID, rowMapper, sessionId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findUserByStatus(String status) {
        return jdbcTemplate.query(FIND_BY_STATUS, rowMapper, status);
    }
}
