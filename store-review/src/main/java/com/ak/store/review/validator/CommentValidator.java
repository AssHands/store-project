package com.ak.store.review.validator;

import com.ak.store.review.model.command.WriteCommentCommand;
import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.document.ReviewStatus;
import com.ak.store.review.repository.CommentRepo;
import com.ak.store.review.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CommentValidator {
    private final ReviewRepo reviewRepo;
    private final CommentRepo commentRepo;

    public void validateCreate(WriteCommentCommand command) {
        reviewExist(command.getReviewId());
    }

    public void validateUpdate(WriteCommentCommand command) {
        commentBelongToUser(command.getCommentId(), command.getUserId());
    }

    public void validateDelete(WriteCommentCommand command) {
        commentBelongToUser(command.getCommentId(), command.getUserId());

    }

    private void reviewExist(ObjectId reviewId) {
        var review = reviewRepo.findById(reviewId);
        var isExist = review.filter(value -> value.getStatus() == ReviewStatus.COMPLETED).isPresent();

        if (!isExist) {
            throw new RuntimeException("review not exist");
        }
    }

    private void commentBelongToUser(ObjectId commentId, UUID userId) {
        var comment = findOneById(commentId);

        if(comment.getUserId().equals(userId)) {
            throw new RuntimeException("comment does not belong to user");
        }
    }

    private Comment findOneById(ObjectId commentId) {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("comment not found"));
    }
}
