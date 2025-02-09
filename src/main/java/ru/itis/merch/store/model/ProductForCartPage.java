package ru.itis.merch.store.model;

public record ProductForCartPage (
        Long cartItemId,
        Long productId,
        String productName,
        Long productPrice,
        String mainPhotoUrl,
        Long specificationId,
        String specificationName,
        Long quantityInStock,
        Long quantity
) {
}
