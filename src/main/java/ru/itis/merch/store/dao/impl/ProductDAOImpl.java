package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.ProductDAO;
import ru.itis.merch.store.entity.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {

    // language=sql
    private static final String FIND_ALL_PRODUCTS_WITH_PAGINATION = "SELECT * FROM product OFFSET ? LIMIT ?";
    // language=sql
    private static final String FIND_PRODUCT_BY_ID = "SELECT * FROM product WHERE id = ?";
    // language=sql
    private static final String FIND_ALL_BY_IDS = "SELECT * FROM product WHERE id IN (%s)";
    // language=sql
    private static final String SAVE_AND_RETURN_ID = "INSERT INTO product (price, name, description) VALUES (?, ?, ?) RETURNING id";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> rowMapper;

    @Override
    public void save(Product object) {

    }

    @Override
    public Optional<Product> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_PRODUCT_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void updateById(Long aLong, Product object) {

    }

    @Override
    public List<Product> findAllWithPagination(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return jdbcTemplate.query(FIND_ALL_PRODUCTS_WITH_PAGINATION, rowMapper, offset, pageSize);
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(String.format(FIND_ALL_BY_IDS, inSql), rowMapper, ids.toArray());

    }

    @Override
    public Long saveAndReturnId(Product product) {
        return jdbcTemplate
                .queryForObject(SAVE_AND_RETURN_ID,
                        (rs, rowNum) -> rs.getLong(1),
                        product.getPrice(), product.getName(), product.getDescription());
    }
}
