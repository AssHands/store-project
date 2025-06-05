package com.ak.store.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private ObjectId id;

    private UUID userId;

    private ObjectId reviewId;

    private String text;

    private LocalDateTime time;
}