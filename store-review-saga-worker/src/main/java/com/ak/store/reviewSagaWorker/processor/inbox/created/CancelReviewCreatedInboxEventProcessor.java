package com.ak.store.reviewSagaWorker.processor.inbox.created;

import com.ak.store.kafka.storekafkastarter.util.JsonMapperKafka;
import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewSnapshot;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEvent;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEventStatus;
import com.ak.store.reviewSagaWorker.model.inbox.InboxEventType;
import com.ak.store.reviewSagaWorker.processor.inbox.InboxEventProcessor;
import com.ak.store.reviewSagaWorker.service.InboxEventReaderService;
import com.ak.store.reviewSagaWorker.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelReviewCreatedInboxEventProcessor implements InboxEventProcessor {
    private final JsonMapperKafka jsonMapperKafka;
    private final InboxEventReaderService inboxEventReaderService;
    private final ReviewService reviewService;

    @Transactional
    @Override
    public void process(InboxEvent event) {
        var snapshot = jsonMapperKafka.fromJson(event.getPayload(), ReviewSnapshot.class);

        try {
            reviewService.cancelOneCreated(new ObjectId(snapshot.getId()));
            inboxEventReaderService.markOneAs(event, InboxEventStatus.SUCCESS);
        } catch (Exception e) {
            inboxEventReaderService.markOneAsFailure(event);
        }
    }

    @Override
    public InboxEventType getType() {
        return InboxEventType.CANCEL_REVIEW_CREATED;
    }
}
