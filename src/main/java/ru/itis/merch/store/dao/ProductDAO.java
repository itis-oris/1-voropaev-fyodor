package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.Product;

import java.util.List;

public interface ProductDAO extends CrudDAO<Product, Long> {
    List<Product> findAllWithPagination(int page, int pageSize);
    List<Product> findAllByIds(List<Long> ids);

    Long saveAndReturnId(Product product);
}
