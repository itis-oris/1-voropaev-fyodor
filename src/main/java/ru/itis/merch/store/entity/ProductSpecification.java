package ru.itis.merch.store.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSpecification {

    private Long id;
    private String name;
    private String description;
    private Long quantityInStock;
    private Long productId;
}
