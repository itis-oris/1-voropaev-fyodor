package ru.itis.merch.store.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPhoto {

    private Long id;
    private String url;
    private Long order;
    private Long productId;
}
