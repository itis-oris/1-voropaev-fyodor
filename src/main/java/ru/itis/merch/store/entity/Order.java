package ru.itis.merch.store.entity;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long id;
    private Long userId;
    private String status;
    private String description;
    private Timestamp creationTime;
    private Timestamp updateTime;
}
