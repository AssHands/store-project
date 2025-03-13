package com.ak.store.outboxScheduler.processor.impl;

import com.ak.store.common.event.catalogue.ProductUpdatedEvent;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.outboxScheduler.kafka.catalogue.ProductProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.processor.OutboxTaskProcessor;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class ProductUpdatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final ProductProducerKafka productProducerKafka;

    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            ProductUpdatedEvent productUpdatedEvent = new ProductUpdatedEvent(
                    new Gson().fromJson(task.getPayload(), ProductRichView.class)
            );

            productProducerKafka.send(productUpdatedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.PRODUCT_UPDATED;
    }
}
