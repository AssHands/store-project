package com.ak.store.outboxScheduler.processor.catalogue;

import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.model.catalogue.snapshot.ProductSnapshotPayload;
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
public class ProductDeletedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final ProductProducerKafka productProducerKafka;

    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(
                    task.getId(), new Gson().fromJson(task.getPayload(), ProductSnapshotPayload.class)
            );

            productProducerKafka.send(productDeletedEvent);
        }
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.PRODUCT_DELETED;
    }
}
