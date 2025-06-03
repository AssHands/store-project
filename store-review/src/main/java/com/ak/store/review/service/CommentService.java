package com.ak.store.review.service;

import com.ak.store.review.mapper.CommentMapper;
import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.dto.CommentDTO;
import com.ak.store.review.model.dto.write.CommentWriteDTO;
import com.ak.store.review.repository.CommentRepo;
import com.ak.store.review.validator.service.CommentServiceValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepo commentRepo;
    private final CommentServiceValidator commentValidator;

    private Comment findOneById(String id) {
        return commentRepo.findById(new ObjectId(id))
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    public List<CommentDTO> findAllByReviewId(String reviewId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentMapper.toCommentDTO(commentRepo.findAllByReviewId(reviewId, pageable));
    }

    public CommentDTO findOne(String commentId) {
        return commentMapper.toCommentDTO(findOneById(commentId));
    }

    public CommentDTO createOne(UUID userId, CommentWriteDTO request) {
        commentValidator.validateCreating(request);
        var comment = commentMapper.toComment(request);

        comment.setTime(LocalDateTime.now());
        comment.setUserId(userId);

        return commentMapper.toCommentDTO(commentRepo.save(comment));
    }

    public CommentDTO updateOne(UUID userId, String commentId, CommentWriteDTO request) {
        commentValidator.validateUpdating(commentId, userId);
        var comment = findOneById(commentId);

        updateOneFromDTO(comment, request);

        return commentMapper.toCommentDTO(commentRepo.save(comment));
    }

    public void deleteOne(UUID userId, String commentId) {
        commentValidator.validateDeleting(commentId, userId);
        commentRepo.deleteById(new ObjectId(commentId));
    }

    public void deleteAllByReviewId(String reviewId) {
        commentRepo.deleteAllByReviewId(reviewId);
    }

    private void updateOneFromDTO(Comment comment, CommentWriteDTO request) {
        if (request.getText() != null) {
            comment.setText(request.getText());
        }
    }
}
