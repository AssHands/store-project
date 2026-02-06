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
public class AddProductGradeInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final RatingUpdaterService ratingUpdaterService;
    private final InboxEventReaderService inboxEventReaderService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var request = jsonMapperKafka.fromJson(event.getPayload(), ReviewCreatedSnapshot.class);

        try {
            ratingUpdaterService.createOne(request.getProductId(), request.getGrade());
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            //todo сделать логику retry. сейчас в случае неудачи - событие сразу помечается как неудачное
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.ADD_PRODUCT_GRADE;
    }
}
