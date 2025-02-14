package com.ak.store.consumer.model;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = { "consumer", "review" })
@EqualsAndHashCode(exclude = { "consumer", "review" })
@Entity
public class CommentReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    private String text;

    private Integer amountLikes;

    private Integer amountDislikes;
}
