package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.OrderDAO;
import ru.itis.merch.store.entity.Order;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderDAOImpl implements OrderDAO {

    // language=sql
    private static final String SAVE_AND_RETURN_ID = "INSERT INTO \"order\" (user_id, status, update_date_time, description) VALUES (?, ?, CURRENT_TIMESTAMP, ?) RETURNING id";
    // language=sql
    private static final String FIND_BY_USER_ID = "SELECT * FROM \"order\" WHERE user_id = ?";


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Order> orderRowMapper;

    @Override
    public void save(Order object) {

    }

    @Override
    public Optional<Order> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void updateById(Long aLong, Order object) {

    }

    @Override
    public Long saveAndReturnId(Order order) {
        return jdbcTemplate
                .queryForObject(SAVE_AND_RETURN_ID,
                        (rs, rowNum) -> rs.getLong(1),
                        order.getUserId(), order.getStatus(), order.getDescription());
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jdbcTemplate.query(FIND_BY_USER_ID, (rs, rowNum) -> new Order(rs.getLong("id"), rs.getLong("user_id"), rs.getString("status"), rs.getString("description"), rs.getTimestamp("creation_date_time"), rs.getTimestamp("update_date_time")), userId);
    }
}
