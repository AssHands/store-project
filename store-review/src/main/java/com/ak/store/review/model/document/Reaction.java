package com.ak.store.review.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reaction")
public class Reaction {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @Field(name = "review_id")
    private ObjectId reviewId;

    @Indexed(unique = true)
    @Field(name = "user_id")
    private UUID userId;

    @Field(name = "is_like")
    private Boolean isLike;
}