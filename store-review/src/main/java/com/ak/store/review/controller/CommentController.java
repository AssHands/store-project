package com.ak.store.review.controller;

import com.ak.store.review.facade.CommentFacade;
import com.ak.store.review.mapper.CommentMapper;
import com.ak.store.review.model.command.WriteCommentCommand;
import com.ak.store.review.model.form.CommentForm;
import com.ak.store.review.model.view.CommentView;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
    public List<CommentView> findAllByReviewId(@RequestParam ObjectId reviewId) {
        return commentFacade.findAllByReviewId(reviewId).stream()
                .map(commentMapper::toView)
                .toList();
    }

    @PostMapping
    public String createOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody CommentForm form) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var command = commentMapper.toWriteCommand(null, userId, form);
        return commentFacade.createOne(command).toString();
    }

    @PatchMapping("update")
    public String updateOne(@AuthenticationPrincipal Jwt accessToken, @RequestBody CommentForm form) {
        UUID userId = UUID.fromString(accessToken.getSubject());
        var command = commentMapper.toWriteCommand(form.getCommentId(), userId, form);
        return commentFacade.updateOne(command).toString();
    }

    @DeleteMapping("{commentId}")
    public String deleteOne(@AuthenticationPrincipal Jwt accessToken, @PathVariable ObjectId commentId) {
        var command = WriteCommentCommand.builder()
                .commentId(commentId)
                .userId(UUID.fromString(accessToken.getSubject()))
                .build();

        return commentFacade.deleteOne(command).toString();
    }
}