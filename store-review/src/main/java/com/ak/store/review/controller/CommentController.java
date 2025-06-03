package com.ak.store.review.controller;

import com.ak.store.review.facade.CommentFacade;
import com.ak.store.review.mapper.CommentMapper;
import com.ak.store.review.model.form.CommentForm;
import com.ak.store.review.model.view.CommentView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/review/comments")
public class CommentController {
    private final CommentFacade commentFacade;
    private final CommentMapper commentMapper;

    @GetMapping
    public List<CommentView> findAllByReviewId(@RequestParam String reviewId,
                                               @RequestParam int page, @RequestParam int size) {
        return commentMapper.toCommentView(commentFacade.findAllByReviewId(reviewId, page, size));
    }

    @PostMapping
    public String createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody CommentForm request) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var comment = commentFacade.createOne(userId, commentMapper.toCommentWriteDTO(request));

        return comment.getId();
    }

    @PatchMapping("{commentId}")
    public String updateOne(@AuthenticationPrincipal Jwt accessToken,
                            @PathVariable String commentId, @RequestBody CommentForm request) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var comment = commentFacade.updateOne(userId, commentId, commentMapper.toCommentWriteDTO(request));
        return comment.getId();
    }

    @DeleteMapping("{commentId}")
    public void deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable String commentId) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        commentFacade.deleteOne(userId, commentId);
    }
}