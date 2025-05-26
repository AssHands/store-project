package com.ak.store.catalogueOutbox.processor.catalogue;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
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
public class ProductDeletedOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;

    @Override
    public void process(OutboxEvent task) {
        ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(
                task.getId(), new Gson().fromJson(task.getPayload(), ProductSnapshotPayload.class));

        String productId = productDeletedEvent.getPayload().getProduct().getId().toString();
        eventProducerKafka.send(productDeletedEvent, productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_DELETED;
    }
}
