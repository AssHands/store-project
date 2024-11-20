package com.ak.store.common.dto.review;

import com.ak.store.common.dto.review.Review;
import com.ak.store.common.entity.user.User;

import java.time.LocalDateTime;

public class CommentReview {
    private Review review;
    private User user;
    private LocalDateTime createdAt;
    private String text;
    private int amountLikes;
    private int amountDislikes;
}
