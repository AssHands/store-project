package com.ak.store.review.repository;

import org.bson.types.ObjectId;

public interface ReviewRepoCustom {
    void incrementCommentAmount(ObjectId reviewId);

    void decrementCommentAmount(ObjectId reviewId);

    void incrementLikeAmount(ObjectId reviewId);

    void decrementLikeAmount(ObjectId reviewId);

    void incrementDislikeAmount(ObjectId reviewId);

    void decrementDislikeAmount(ObjectId reviewId);

    void incrementLikeAmountAndDecrementDislikeAmount(ObjectId reviewId);

    void decrementLikeAmountAndIncrementDislikeAmount(ObjectId reviewId);
}