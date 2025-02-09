package ru.itis.merch.store.model;

public record ProductForProfilePage (

        Long productId,
        String productName,
        String mainPhotoUrl,
        String productSpecificationName,
        Long quantity

) {
}
