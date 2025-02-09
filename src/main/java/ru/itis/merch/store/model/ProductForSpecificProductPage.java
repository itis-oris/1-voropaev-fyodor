package ru.itis.merch.store.model;

import ru.itis.merch.store.entity.ProductSpecification;

import java.util.List;

public record ProductForSpecificProductPage (
        Long id,
        String name,
        Long price,
        String description,
        List<String> photoUrls,
        List<ProductSpecification> productSpecifications
) {
}
