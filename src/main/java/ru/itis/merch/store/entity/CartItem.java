package ru.itis.merch.store.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    private Long id;
    private Long userId;
    private Long productSpecificationId;
    private Long quantity;
}
