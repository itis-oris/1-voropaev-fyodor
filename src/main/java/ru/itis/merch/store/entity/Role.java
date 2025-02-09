package ru.itis.merch.store.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    private Long id;
    private String name;
}
