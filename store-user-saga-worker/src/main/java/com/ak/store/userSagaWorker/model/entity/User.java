package com.ak.store.userSagaWorker.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {
    @Id
    private UUID id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}