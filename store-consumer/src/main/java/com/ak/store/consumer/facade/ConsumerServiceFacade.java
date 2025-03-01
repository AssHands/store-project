package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.common.util.SagaBuilder;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.service.ConsumerService;
import com.ak.store.consumer.service.EmailSender;
import com.ak.store.consumer.service.KeycloakService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class ConsumerServiceFacade {
    private final ConsumerService consumerService;
    private final ConsumerMapper consumerMapper;
    private final KeycloakService keycloakService;
    private final EmailSender emailSender;

    @Transactional
    public String createOne(ConsumerDTO consumerDTO) {
        String id = "";
        String verificationCode = UUID.randomUUID().toString();

        try {
            id = keycloakService.createOneConsumer(consumerDTO);
            Consumer consumer = consumerService.createOne(id, consumerDTO);
            //CompletableFuture.runAsync(() -> emailSender.sendVerificationEmail(consumer, verificationCode));
            consumerService.makeVerificationCode(id, verificationCode);
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

    public Boolean existOne(String id) {
        return consumerService.existOne(id);
    }

    @Transactional
    public String verifyOne(String code) {
        String id = consumerService.verifyOne(code).getId().toString();
        keycloakService.verifyOneConsumer(id);
        return id;
    }
}
