package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.dto.AddProductGradeRequest;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.entity.InboxEventType;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.RatingUpdaterService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddProductGradeInboxEventProcessor implements InboxEventProcessor {
    private final Gson gson;
    private final RatingUpdaterService ratingUpdaterService;

    @Override
    public void process(InboxEvent event) {
        var request = gson.fromJson(event.getPayload(), AddProductGradeRequest.class);
        ratingUpdaterService.createOne(request.getProductId(), request.getGrade());
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.ADD_PRODUCT_GRADE;
    }
}
