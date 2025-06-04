package com.ak.store.review.repository;

public interface ReviewRepoCustom {
    void incrementOneCommentAmount(String reviewId);

    void decrementOneCommentAmount(String reviewId);

    void incrementOneLikeAmount(String reviewId);

    void decrementOneLikeAmount(String reviewId);

    void incrementOneDislikeAmount(String reviewId);

    void decrementOneDislikeAmount(String reviewId);
}