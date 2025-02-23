package com.ak.store.consumer.model.projection;

import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.model.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWithCommentCountProjection {
    private Review review;
    private Consumer consumer;
    private int amountComment;
}
