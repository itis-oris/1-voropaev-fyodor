package ru.itis.merch.store.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.merch.store.dao.ProductPhotoDAO;
import ru.itis.merch.store.entity.ProductPhoto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductPhotoDAOImpl implements ProductPhotoDAO {

    // language=sql
    private static final String FIND_PHOTOS_BY_PRODUCT_ID = "SELECT * FROM product_photo WHERE product_id = ? ORDER BY \"order\"";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ProductPhoto> rowMapper;

    @Override
    public void save(ProductPhoto object) {

    }

    @Override
    public Optional<ProductPhoto> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<ProductPhoto> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void updateById(Long aLong, ProductPhoto object) {

    }

    @Override
    public List<ProductPhoto> findByProductId(Long productId) {
        return jdbcTemplate.query(FIND_PHOTOS_BY_PRODUCT_ID, rowMapper, productId);
    }
}
