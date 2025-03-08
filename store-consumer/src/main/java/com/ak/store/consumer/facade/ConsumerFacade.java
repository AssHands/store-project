package com.ak.store.consumer.facade;

import com.ak.store.common.event.consumer.ConsumerVerifyEvent;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.consumer.kafka.ConsumerProducerKafka;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.service.ConsumerService;
import com.ak.store.consumer.service.KeycloakService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConsumerFacade {
    private final ConsumerService consumerService;
    private final ConsumerMapper consumerMapper;
    private final KeycloakService keycloakService;
    private final ConsumerProducerKafka consumerProducerKafka;

    @Transactional
    public String createOne(ConsumerDTO consumerDTO) {
        String id = "";
        String verificationCode = UUID.randomUUID().toString();

        try {
            id = keycloakService.createOneConsumer(consumerDTO);
            Consumer consumer = consumerService.createOne(id, consumerDTO);
            consumerService.makeVerificationCode(id, verificationCode, consumer.getEmail());
            consumerProducerKafka.send(new ConsumerVerifyEvent(verificationCode, consumer.getEmail()));
        } catch (Exception e) {
            if (!id.isBlank())
                keycloakService.deleteOneConsumer(id);

            throw new RuntimeException("error while creating consumer");
        }

        return id;
    }

    public ConsumerPoorView findOne(String id) {
        return consumerMapper.mapToConsumerPoorView(consumerService.findOne(id));
    }

    @Transactional
    //todo: не удлаять его отзывы.
    public void deleteOne(String id) {
        keycloakService.deleteOneConsumer(id);
        consumerService.deleteOne(id);
    }

    @Transactional
    public String updateOne(String id, ConsumerDTO consumerDTO) {
        keycloakService.updateOneConsumer(id, consumerDTO);
        return consumerService.updateOne(id, consumerDTO).getId().toString();
    }

    @Transactional
    public String updateOneEmail(String id, String email) {
        String verificationCode = UUID.randomUUID().toString();

        consumerService.makeVerificationCode(id, verificationCode, email);
        consumerProducerKafka.send(new ConsumerVerifyEvent(verificationCode, email));

        return id;
    }

    public Boolean existOne(String id) {
        return consumerService.existOne(id);
    }

    @Transactional
    public String verifyOne(String code) {
        Consumer consumer = consumerService.verifyOne(code);
        String id = consumer.getId().toString();
        keycloakService.verifyOneConsumer(id, consumer.getEmail());
        return id;
    }
}
