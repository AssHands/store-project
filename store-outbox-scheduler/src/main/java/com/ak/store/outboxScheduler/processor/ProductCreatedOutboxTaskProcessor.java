package com.ak.store.outboxScheduler.processor;

import com.ak.store.common.event.catalogue.ProductCreatedEvent;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.outboxScheduler.kafka.ProductProducerKafka;
import com.ak.store.outboxScheduler.model.OutboxTask;
import com.ak.store.outboxScheduler.model.OutboxTaskType;
import com.ak.store.outboxScheduler.service.OutboxTaskService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductCreatedOutboxTaskProcessor implements OutboxTaskProcessor {
    private final ProductProducerKafka productProducerKafka;
    private final OutboxTaskService outboxTaskService;

    @Override
    public void process(List<OutboxTask> tasks) {
        for (OutboxTask task : tasks) {
            ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                    new Gson().fromJson(task.getPayload(), ProductRichView.class)
            );

            productProducerKafka.send(productCreatedEvent);
        }

        //todo: если выкинется ошибка, сообщение уже будет отправлено
        outboxTaskService.markAllTaskAsCompleted(tasks);
    }

    @Override
    public OutboxTaskType getType() {
        return OutboxTaskType.PRODUCT_CREATED;
    }
}
