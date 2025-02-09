package ru.itis.merch.store.service;

import ru.itis.merch.store.model.OrderForProfilePage;

import java.util.List;

public interface OrderService {
    void createOrder(Long userId);
    List<OrderForProfilePage>  findOrdersByUserId(Long userId);
}
