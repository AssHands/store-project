package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.dto.UpdateProductGradeRequest;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.RatingUpdaterService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateProductGradeInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final RatingUpdaterService ratingUpdaterService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), UpdateProductGradeRequest.class);
        var review = request.getReview();
        ratingUpdaterService.updateOne(review.getProductId(), request.getNewGrade(), review.getGrade());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.UPDATE_PRODUCT_GRADE;
    }
}
