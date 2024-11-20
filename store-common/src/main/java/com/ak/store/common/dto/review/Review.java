package com.ak.store.common.dto.review;

import com.ak.store.common.entity.user.User;

import java.time.LocalDateTime;

public class Review {
    private User user;
    private LocalDateTime createdAt;
    private String advantages;
    private String disadvantages;
    private String text;
    private int grade;
    private int amountLikes;
    private int amountDislikes;
}