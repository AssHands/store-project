package com.ak.store.reviewSagaWorker.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "review")
public class Review {
    @Id
    private ObjectId id;

    @Field(name = "user_id")
    private UUID userId;

    @Field(name = "product_id")
    private Long productId;

    private String text;

    private String advantages;

    private String disadvantages;

    @Field(name = "comment_amount")
    private Integer commentAmount;

    private Integer grade;

    @Field(name = "like_amount")
    private Integer likeAmount;

    @Field(name = "dislike_amount")
    private Integer dislikeAmount;

    private ReviewStatus status;

    private LocalDateTime time;
}