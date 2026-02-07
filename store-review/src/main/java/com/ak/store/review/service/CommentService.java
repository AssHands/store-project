package com.ak.store.review.service;

import com.ak.store.review.mapper.CommentMapper;
import com.ak.store.review.model.command.WriteCommentCommand;
import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.dto.CommentDTO;
import com.ak.store.review.repository.CommentRepo;
import com.ak.store.review.validator.CommentValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepo commentRepo;
    private final CommentValidator commentValidator;

    private final ReviewService reviewService;

    private Comment findOneById(ObjectId id) {
        return commentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<CommentDTO> findAllByReviewId(ObjectId reviewId) {
        return commentRepo.findAllByReviewId(reviewId).stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    public CommentDTO findOne(ObjectId commentId) {
        return commentMapper.toDTO(findOneById(commentId));
    }

    public CommentDTO createOne(WriteCommentCommand command) {
        commentValidator.validateCreate(command);

        var comment = commentMapper.toDocument(command);
        comment.setTime(LocalDateTime.now());
        reviewService.updateCommentCounter(command.getReviewId(), +1);

        return commentMapper.toDTO(commentRepo.save(comment));
    }

    public CommentDTO updateOne(WriteCommentCommand command) {
        commentValidator.validateUpdate(command);

        var comment = findOneById(command.getCommentId());
        comment.setText(command.getText());

        return commentMapper.toDTO(commentRepo.save(comment));
    }

    public void deleteOne(WriteCommentCommand command) {
        commentValidator.validateDelete(command);

        reviewService.updateCommentCounter(command.getReviewId(), -1);

        commentRepo.deleteById(command.getCommentId());
    }
}
