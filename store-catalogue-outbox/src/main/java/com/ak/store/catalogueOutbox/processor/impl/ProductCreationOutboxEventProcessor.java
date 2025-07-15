package com.ak.store.catalogueOutbox.processor.impl;

import com.ak.store.catalogueOutbox.kafka.EventProducerKafka;
import com.ak.store.catalogueOutbox.mapper.JsonMapper;
import com.ak.store.catalogueOutbox.model.OutboxEvent;
import com.ak.store.catalogueOutbox.model.OutboxEventType;
import com.ak.store.catalogueOutbox.processor.OutboxEventProcessor;
import com.ak.store.common.kafka.catalogue.ProductCreationEvent;
import com.ak.store.common.kafka.user.UserCreationEvent;
import com.ak.store.common.saga.SagaRequestEvent;
import com.ak.store.common.snapshot.catalogue.ProductCreationSnapshot;
import com.ak.store.common.snapshot.user.UserCreationSnapshot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductCreationOutboxEventProcessor implements OutboxEventProcessor {
    private final EventProducerKafka eventProducerKafka;
    private final Gson gson;
    private final JsonMapper jsonMapper;

    @Override
    public void process(OutboxEvent event) throws JsonProcessingException {
        var message = new ProductCreationEvent(event.getId(),
                gson.fromJson(event.getPayload(), ProductCreationSnapshot.class));

        var request = SagaRequestEvent.builder()
                .sagaId(event.getId())
                .request(jsonMapper.toJsonNode(message.getRequest()))
                .build();

        String productId = message.getRequest().getPayload().getProduct().getId().toString();
        eventProducerKafka.send(request, getType(), productId);
    }

    @Override
    public OutboxEventType getType() {
        return OutboxEventType.PRODUCT_CREATION;
    }
}
