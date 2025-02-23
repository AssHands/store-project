package com.ak.store.consumer.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = { "consumer" })
@EqualsAndHashCode(exclude = { "consumer" })
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Consumer consumer;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReview> comments = new ArrayList<>();

    private Long productId;

    private String text;

    private String advantages;

    private String disadvantages;

    private Integer grade;

    private Integer amountLikes;

    private Integer amountDislikes;
}
