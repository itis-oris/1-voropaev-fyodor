package ru.itis.merch.store.service;

import ru.itis.merch.store.entity.Product;
import ru.itis.merch.store.model.ProductForIndexPage;
import ru.itis.merch.store.model.ProductForSpecificProductPage;

import java.util.List;

public interface ProductService {
    List<ProductForIndexPage> findAllProducts(int page, int pageSize);
    ProductForSpecificProductPage findProductById(Long productId);
    Product createProduct(String name, Long price, String description);
}
