package com.ak.store.review.facade;

import com.ak.store.review.model.command.WriteCommentCommand;
import com.ak.store.review.model.dto.CommentDTO;
import com.ak.store.review.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentFacade {
    private final CommentService commentService;

    public List<CommentDTO> findAllByReviewId(ObjectId reviewId) {
        return commentService.findAllByReviewId(reviewId);
    }

    public ObjectId createOne(WriteCommentCommand command) {
        return commentService.createOne(command).getId();
    }

    public ObjectId updateOne(WriteCommentCommand command) {
        return commentService.updateOne(command).getId();
    }

    public String deleteOne(WriteCommentCommand command) {
        commentService.deleteOne(command);
        return command.getCommentId().toString();
    }
}
