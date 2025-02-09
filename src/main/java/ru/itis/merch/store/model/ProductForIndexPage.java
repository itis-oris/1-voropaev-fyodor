package ru.itis.merch.store.model;

public record ProductForIndexPage (
        Long id,
        String name,
        Long price,
        String description,
        String mainPhotoUrl
) {}
