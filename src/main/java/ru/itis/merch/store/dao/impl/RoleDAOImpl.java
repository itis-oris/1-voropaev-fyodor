package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.RoleDAO;
import ru.itis.merch.store.entity.Role;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RoleDAOImpl implements RoleDAO {

    // language=sql
    private static final String FIND_ROLE_BY_ID = "SELECT * FROM role WHERE id = ?";
    // language=sql
    private static final String FIND_ROLE_BY_NAME = "SELECT  * FROM role WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Role> rowMapper;

    @Override
    public void save(Role object) {

    }

    @Override
    public Optional<Role> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_ROLE_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Role> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void updateById(Long aLong, Role object) {

    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_ROLE_BY_NAME, rowMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
