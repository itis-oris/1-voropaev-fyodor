package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.CartItemDAO;
import ru.itis.merch.store.entity.CartItem;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CartItemDAOImpl implements CartItemDAO {

    // language=sql
    private static final String FIND_CART_ITEMS_BY_USER_ID = "SELECT * FROM cart_item WHERE user_id = ?";
    // language=sql
    private static final String FIND_CART_ITEM_BY_USER_ID_AND_SPECIFICATION_ID = "SELECT * FROM cart_item WHERE user_id = ? AND product_specification_id = ?";
    // language=sql
    private static final String SAVE_CART_ITEM = "INSERT INTO cart_item (user_id, product_specification_id, quantity) VALUES (?, ?, ?)";
    // language=sql
    private static final String UPDATE_CART_ITEM = "UPDATE cart_item SET quantity = ? WHERE id = ?";
    // language=sql
    private static final String FIND_BY_ID = "SELECT * FROM cart_item WHERE id = ?";
    // language=sql
    private static final String DELETE_BY_ID = "DELETE FROM cart_item WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CartItem> rowMapper;

    @Override
    public void save(CartItem object) {
        jdbcTemplate.update(SAVE_CART_ITEM, object.getUserId(), object.getProductSpecificationId(), object.getQuantity());
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<CartItem> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void updateById(Long id, CartItem object) {
        jdbcTemplate.update(UPDATE_CART_ITEM, object.getQuantity(), id);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return jdbcTemplate.query(FIND_CART_ITEMS_BY_USER_ID, rowMapper, userId);
    }

    @Override
    public Optional<CartItem> findByUserIdAndProductSpecificationId(Long userId, Long specificationId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_CART_ITEM_BY_USER_ID_AND_SPECIFICATION_ID, rowMapper, userId, specificationId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
