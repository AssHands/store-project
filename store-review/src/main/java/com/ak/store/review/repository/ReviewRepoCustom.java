package com.ak.store.review.repository;

import org.bson.types.ObjectId;

public interface ReviewRepoCustom {
    void incrementOneCommentAmount(ObjectId reviewId);

    void decrementOneCommentAmount(ObjectId reviewId);

    void incrementOneLikeAmount(ObjectId reviewId);

    void decrementOneLikeAmount(ObjectId reviewId);

    void incrementOneDislikeAmount(ObjectId reviewId);

    void decrementOneDislikeAmount(ObjectId reviewId);

    void incrementOneLikeAmountAndDecrementDislikeAmount(ObjectId reviewId);

    void decrementOneLikeAmountAndIncrementDislikeAmount(ObjectId reviewId);
}