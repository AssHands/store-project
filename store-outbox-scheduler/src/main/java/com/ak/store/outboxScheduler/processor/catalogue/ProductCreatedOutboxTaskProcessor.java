package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import com.ak.store.outboxScheduler.kafka.EventProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductCreatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxTask task) {
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), ProductSnapshotPayload.class));

        String productId = productCreatedEvent.getPayload().getProduct().getId().toString();
        eventProducerKafka.send(productCreatedEvent, productId);
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.PRODUCT_CREATED;
    }
}
