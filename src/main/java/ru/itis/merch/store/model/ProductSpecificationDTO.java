package ru.itis.merch.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDTO {
    private String name;
    private String description;
    private Long quantity;
}