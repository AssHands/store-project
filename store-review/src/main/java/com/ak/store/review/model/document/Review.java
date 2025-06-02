package com.ak.store.review.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    @Field(name = "amount_comment")
    private Integer amountComment;

    private Integer grade;
}