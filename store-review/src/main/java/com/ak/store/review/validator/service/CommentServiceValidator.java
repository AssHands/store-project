package com.ak.store.review.validator.service;

import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.dto.write.CommentWriteDTO;
import com.ak.store.review.repository.CommentRepo;
import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CommentServiceValidator {
    private final ReviewRepo reviewRepo;
    private final CommentRepo commentRepo;

    public void validateCreating(CommentWriteDTO request) {
        if(!isReviewExist(request.getReviewId())) {
            throw new RuntimeException("review not exist");
        }
    }

    public void validateUpdating(String commentId, UUID userId) {
        if(!isCommentBelongToUser(commentId, userId)) {
            throw new RuntimeException("comment do not belong to user");
        }
    }

    public void validateDeleting(String commentId, UUID userId) {
        if(!isCommentBelongToUser(commentId, userId)) {
            throw new RuntimeException("comment do not belong to user");
        }
    }

    private boolean isReviewExist(String reviewId) {
        return reviewRepo.findById(new ObjectId(reviewId)).isPresent();
    }

    private boolean isCommentBelongToUser(String commentId, UUID userId) {
        var comment = findOneById(commentId);
        return comment.getUserId().equals(userId);
    }

    private Comment findOneById(String commentId) {
        return commentRepo.findById(new ObjectId(commentId))
                .orElseThrow(() -> new RuntimeException("comment not found"));
    }
}
