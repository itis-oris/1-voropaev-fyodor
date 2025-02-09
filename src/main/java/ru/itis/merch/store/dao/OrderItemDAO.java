package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.OrderItem;

import java.util.List;

public interface OrderItemDAO extends CrudDAO<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long id);
}
