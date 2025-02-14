package com.ak.store.consumer.util;

import com.ak.store.common.model.consumer.dto.ReviewDTO;
import com.ak.store.common.model.consumer.view.CartView;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.model.Cart;
import com.ak.store.consumer.model.Consumer;
import com.ak.store.consumer.model.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    //todo: он зачем то создает объект ProductView в CartView, который я затем перезаписываю. исправить
    public CartView mapToCartView(Cart cart) {
        return modelMapper.map(cart, CartView.class);
    }

    public Review mapToReview(ReviewDTO reviewDTO, Long productId, Long consumerId) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setProductId(productId);
        review.setConsumer(Consumer.builder().id(consumerId).build());
        return review;
    }

    public ReviewView mapToReviewView(Review review) {
        return modelMapper.map(review, ReviewView.class);
    }
}
