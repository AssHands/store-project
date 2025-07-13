package com.ak.store.review.validator.service;

import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.document.ReviewStatus;
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

    public void validateUpdating(UUID userId, ObjectId commentId) {
        if(!isCommentBelongToUser(commentId, userId)) {
            throw new RuntimeException("comment do not belong to user");
        }
    }

    public void validateDeleting(UUID userId, ObjectId commentId) {
        if(!isCommentBelongToUser(commentId, userId)) {
            throw new RuntimeException("comment do not belong to user");
        }
    }

    private boolean isReviewExist(ObjectId reviewId) {
        var review = reviewRepo.findById(reviewId);
        return review.filter(value -> value.getStatus() == ReviewStatus.COMPLETED).isPresent();
    }

    private boolean isCommentBelongToUser(ObjectId commentId, UUID userId) {
        var comment = findOneById(commentId);
        return comment.getUserId().equals(userId);
    }

    private Comment findOneById(ObjectId commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("comment not found"));
    }
}
