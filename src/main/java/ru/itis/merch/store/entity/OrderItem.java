package ru.itis.merch.store.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    private Long id;
    private Long orderId;
    private Long productSpecificationId;
    private Long quantity;
}
