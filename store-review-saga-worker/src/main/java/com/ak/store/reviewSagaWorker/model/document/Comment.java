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
@Document(collection = "comment")
public class Comment {
    @Id
    private ObjectId id;

    @Field(name = "user_id")
    private UUID userId;

    @Field(name = "review_id")
    private ObjectId reviewId;

    private String text;

    private LocalDateTime time;
}