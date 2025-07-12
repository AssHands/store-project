package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.dto.DeleteProductGradeRequest;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.RatingUpdaterService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteProductGradeInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final RatingUpdaterService ratingUpdaterService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), DeleteProductGradeRequest.class);
        ratingUpdaterService.deleteOne(request.getProductId(), request.getGrade());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.DELETE_PRODUCT_GRADE;
    }
}
