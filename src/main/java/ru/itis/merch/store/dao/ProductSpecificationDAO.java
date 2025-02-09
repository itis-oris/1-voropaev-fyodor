package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.ProductSpecification;

import java.util.List;

public interface ProductSpecificationDAO extends CrudDAO<ProductSpecification, Long> {

    List<ProductSpecification> findByProductId(Long productId);
    List<ProductSpecification> findAllByIds(List<Long> ids);
}
