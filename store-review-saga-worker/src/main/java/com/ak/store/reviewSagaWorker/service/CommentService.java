package com.ak.store.reviewSagaWorker.service;

import com.ak.store.reviewSagaWorker.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepo commentRepo;

    public void deleteAllByReviewId(ObjectId reviewId) {
        commentRepo.deleteAllByReviewId(reviewId);
    }
}
