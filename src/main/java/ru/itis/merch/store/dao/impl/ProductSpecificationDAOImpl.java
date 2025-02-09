package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.ProductSpecificationDAO;
import ru.itis.merch.store.entity.ProductSpecification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductSpecificationDAOImpl implements ProductSpecificationDAO {

    // language=sql
    private static final String FIND_BY_ID = "SELECT * FROM product_specification WHERE id = ?";
    // language=sql
    private static final String FIND_BY_PRODUCT_ID = "SELECT * FROM product_specification WHERE product_id = ?";
    // language=sql
    private static final String FIND_BY_IDS = "SELECT * FROM product_specification WHERE id IN (%s)";
    // language=sql
    private static final String UPDATE_BY_ID = "UPDATE product_specification SET quantity_in_stock = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ProductSpecification> rowMapper;

    @Override
    public void save(ProductSpecification object) {

    }

    @Override
    public Optional<ProductSpecification> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductSpecification> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void updateById(Long id, ProductSpecification object) {
        jdbcTemplate.update(UPDATE_BY_ID, object.getQuantityInStock(), id);
    }

    @Override
    public List<ProductSpecification> findByProductId(Long productId) {
        return jdbcTemplate.query(FIND_BY_PRODUCT_ID, rowMapper, productId);
    }

    @Override
    public List<ProductSpecification> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return jdbcTemplate.query(String.format(FIND_BY_IDS, inSql), rowMapper, ids.toArray());
    }
}
