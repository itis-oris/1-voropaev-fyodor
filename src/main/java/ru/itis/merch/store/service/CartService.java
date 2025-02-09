package ru.itis.merch.store.service;

import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.model.ProductForCartPage;

import java.util.List;

public interface CartService {

    List<ProductForCartPage> findCartItemsByUserId(Long userId);

    String addProductSpecificationToUserCart(String data, Long userId);

    void changeQuantityOfCartItem(String json, User user);
}
