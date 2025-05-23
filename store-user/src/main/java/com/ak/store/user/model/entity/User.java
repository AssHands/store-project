package com.ak.store.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@ToString(exclude = { "reviews", "comments" })
//@EqualsAndHashCode(exclude = { "reviews", "comments" })
@Table(name = "users")
@Entity
public class User {
    @Id
    private UUID id;

    private String name;

    private String email;

    private String password;

    private Boolean isEnabled;

    private String avatarKey;

//    @Builder.Default
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Review> reviews = new ArrayList<>();
//
//    @Builder.Default
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationCode verificationCode;
}
