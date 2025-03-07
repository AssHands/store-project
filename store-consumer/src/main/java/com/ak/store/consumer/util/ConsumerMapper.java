package com.ak.store.consumer.util;

import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.CommentView;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.model.entity.Cart;
import com.ak.store.consumer.model.entity.Comment;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.model.entity.Review;
import com.ak.store.consumer.model.projection.ReviewWithCommentCountProjection;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConsumerMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ConsumerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Consumer mapToConsumer(ConsumerDTO consumerDTO) {
        return modelMapper.map(consumerDTO, Consumer.class);
    }

    public ConsumerDTO mapToConsumerDTO(Consumer consumer) {
        return modelMapper.map(consumer, ConsumerDTO.class);
    }

    public CartView mapToCartView(Cart cart) {
        return modelMapper.map(cart, CartView.class);
    }

    public Review mapToReview(ReviewDTO reviewDTO, Long productId, String consumerId) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setProductId(productId);
        review.setConsumer(Consumer.builder().id(UUID.fromString(consumerId)).build());
        return review;
    }

    public ReviewView mapToReviewView(Review review) {
        return modelMapper.map(review, ReviewView.class);
    }

    public ReviewView mapToReviewView(ReviewWithCommentCountProjection reviewProjection) {
        var reviewView = modelMapper.map(reviewProjection.getReview(), ReviewView.class);
        reviewView.setAmountComment(reviewProjection.getAmountComment());
        return reviewView;
    }

    public CommentView mapToCommentReviewView(Comment comment) {
        return modelMapper.map(comment, CommentView.class);
    }

    public ConsumerPoorView mapToConsumerPoorView(Consumer consumer) {
        return modelMapper.map(consumer, ConsumerPoorView.class);
    }
}
