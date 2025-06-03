package com.ak.store.review.validator;

import com.ak.store.review.repository.ReactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ReactionServiceValidator {
    private final ReactionRepo reactionRepo;

    public void validateLikingOneReview(UUID userId, String reviewId) {

    }

    public void validateDislikingOneReview(UUID userId, String reviewId) {

    }
}
