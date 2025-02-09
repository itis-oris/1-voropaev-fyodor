package ru.itis.merch.store.model;

import java.sql.Timestamp;
import java.util.List;

public record OrderForProfilePage (
        Long id,
        String status,
        String description,
        Timestamp creationTime,
        Timestamp updateTime,
        List<ProductForProfilePage> products
) {
}
