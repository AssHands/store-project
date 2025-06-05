package com.ak.store.review.facade;

import com.ak.store.review.model.dto.CommentDTO;
import com.ak.store.review.model.dto.write.CommentWriteDTO;
import com.ak.store.review.service.CommentService;
import com.ak.store.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentFacade {
    private final CommentService commentService;
    private final ReviewService reviewService;

    public List<CommentDTO> findAllByReviewId(ObjectId reviewId, int page, int size) {
        return commentService.findAllByReviewId(reviewId, page, size);
    }

    public CommentDTO createOne(UUID userId, CommentWriteDTO request) {
        var comment = commentService.createOne(userId, request);
        reviewService.incrementOneCommentAmount(comment.getReviewId());
        return comment;
    }

    public CommentDTO updateOne(UUID userId, ObjectId commentId, CommentWriteDTO request) {
        return commentService.updateOne(userId, commentId, request);
    }

    public void deleteOne(UUID userId, ObjectId commentId) {
        var comment = commentService.findOne(commentId);
        commentService.deleteOne(userId, commentId);
        reviewService.decrementOneCommentAmount(comment.getReviewId());
    }
}
