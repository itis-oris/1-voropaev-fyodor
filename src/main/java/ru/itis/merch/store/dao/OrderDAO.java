package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.Order;

import java.util.List;

public interface OrderDAO extends CrudDAO<Order, Long> {

    Long saveAndReturnId(Order order);

    List<Order> findByUserId(Long userId);
}
