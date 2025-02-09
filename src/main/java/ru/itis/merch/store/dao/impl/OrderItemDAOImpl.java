package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.OrderItemDAO;
import ru.itis.merch.store.entity.OrderItem;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderItemDAOImpl implements OrderItemDAO {

    // language=sql
    private static final String SAVE_ORDER_ITEM = "INSERT INTO order_item (order_id, product_specification_id, quantity) VALUES (?, ?, ?)";
    // language=sql
    private static final String FIND_BY_ORDER_ID = "SELECT * FROM order_item WHERE order_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderItem> rowMapper;

    @Override
    public void save(OrderItem object) {
        jdbcTemplate.update(SAVE_ORDER_ITEM, object.getOrderId(), object.getProductSpecificationId(), object.getQuantity());
    }

    @Override
    public Optional<OrderItem> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void updateById(Long aLong, OrderItem object) {

    }

    @Override
    public List<OrderItem> findByOrderId(Long id) {
        return jdbcTemplate.query(FIND_BY_ORDER_ID, rowMapper, id);
    }
}
