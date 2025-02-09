package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemDAO extends CrudDAO<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductSpecificationId(Long userId, Long specificationId);

}
