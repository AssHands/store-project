package com.ak.store.consumer.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "carts", "reviews", "commentReviews" })
@EqualsAndHashCode(exclude = { "carts", "reviews", "commentReviews" })
@Entity
public class Consumer {
    @Id
    private UUID id;

    private String name;

    private String email;

    private String password;

    private Boolean enabled;

    private String avatarKey;

    @Builder.Default
    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationCode verificationCode;
}
