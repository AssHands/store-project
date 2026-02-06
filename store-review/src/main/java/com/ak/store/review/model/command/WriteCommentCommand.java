package com.ak.store.review.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteCommentCommand {
    private UUID userId;

    private ObjectId commentId;

    private ObjectId reviewId;

    private String text;
}
