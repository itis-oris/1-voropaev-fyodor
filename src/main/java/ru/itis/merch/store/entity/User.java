package ru.itis.merch.store.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String status;
    private String sessionId;
    private Long activityPoints;
    private Long roleId;
}
