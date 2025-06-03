package com.ak.store.review.repository;

public interface ReviewRepoCustom {
    void incrementOneCommentAmount(String reviewId);

    void decrementOneCommentAmount(String reviewId);

    void incrementOneLikeAmount(String reviewId);

    void incrementOneISLikeAmount(String reviewId);
}