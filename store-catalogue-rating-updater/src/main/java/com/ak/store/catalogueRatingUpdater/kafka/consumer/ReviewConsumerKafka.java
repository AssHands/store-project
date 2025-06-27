package com.ak.store.catalogueRatingUpdater.kafka.consumer;

import com.ak.store.catalogueRatingUpdater.errorHandler.ReviewKafkaErrorHandler;
import com.ak.store.catalogueRatingUpdater.facade.RatingUpdaterFacade;
import com.ak.store.common.kafka.review.ReviewCreatedEvent;
import com.ak.store.common.kafka.review.ReviewDeletedEvent;
import com.ak.store.common.kafka.review.ReviewUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewConsumerKafka {
    private final ReviewKafkaErrorHandler errorHandler;
    private final RatingUpdaterFacade ratingUpdaterFacade;

    //todo не видно ошибкой, если я ловлю их. добавить логи
    @KafkaListener(topics = "${kafka.topics.review-created}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleCreated(List<ReviewCreatedEvent> reviewCreatedEvents, Acknowledgment ack) {
        for (ReviewCreatedEvent event : reviewCreatedEvents) {
            try {
                var review = event.getReview();
                ratingUpdaterFacade.createOne(review.getProductId(), review.getGrade());
            } catch (Exception e) {
                errorHandler.handleCreateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.review-updated}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleUpdated(List<ReviewUpdatedEvent> reviewUpdatedEvents, Acknowledgment ack) {
        for (ReviewUpdatedEvent event : reviewUpdatedEvents) {
            try {
                var review = event.getPayload().getReview();
                ratingUpdaterFacade.updateOne(review.getProductId(), review.getGrade(), event.getPayload().getOldGrade());
            } catch (Exception e) {
                errorHandler.handleUpdateError(event, e);
            }
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topics.review-deleted}", groupId = "${spring.kafka.consumer.group-id}", batch = "true")
    public void handleDeleted(List<ReviewDeletedEvent> reviewDeletedEvents, Acknowledgment ack) {
        for (ReviewDeletedEvent event : reviewDeletedEvents) {
            try {
                var review = event.getReview();
                ratingUpdaterFacade.deleteOne(review.getProductId(), review.getGrade());
            } catch (Exception e) {
                errorHandler.handleDeleteError(event, e);
            }
        }

        ack.acknowledge();
    }
}