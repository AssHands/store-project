package com.ak.store.consumer.model.entity;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = { "consumer", "review" })
@EqualsAndHashCode(exclude = { "consumer", "review" })
@Entity
@Table(name = "comment_review")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    private String text;
}
