package com.ak.store.catalogueOutbox.processor.catalogue;

import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductUpdatedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxEvent task) {
        ProductUpdatedEvent productUpdatedEvent = new ProductUpdatedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), ProductSnapshotPayload.class));

        String productId = productUpdatedEvent.getPayload().getProduct().getId().toString();
        eventProducerKafka.send(productUpdatedEvent, productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_UPDATED;
    }
}
