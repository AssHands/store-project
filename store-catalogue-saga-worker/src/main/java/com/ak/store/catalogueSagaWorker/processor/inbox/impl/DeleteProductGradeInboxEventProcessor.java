package com.ak.store.catalogueSagaWorker.processor.inbox.impl;

import com.ak.store.catalogueSagaWorker.model.inbox.InboxEvent;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.catalogueSagaWorker.model.inbox.InboxEventType;
import com.ak.store.catalogueSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.catalogueSagaWorker.service.InboxEventReaderService;
import com.ak.store.catalogueSagaWorker.service.RatingUpdaterService;
import com.ak.store.kafka.storekafkastarter.JsonMapperKafka;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteProductGradeInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final RatingUpdaterService ratingUpdaterService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), ReviewDeletionSnapshot.class);

        try {
            ratingUpdaterService.deleteOne(request.getProductId(), request.getGrade());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.DELETE_PRODUCT_GRADE;
    }
}
