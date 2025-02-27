package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.consumer.service.ConsumerService;
import com.ak.store.consumer.service.KeycloakService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerServiceFacade {
    private final ConsumerService consumerService;
    private final ConsumerMapper consumerMapper;
    private final KeycloakService keycloakService;

    @Transactional
    public String createOne(ConsumerDTO consumerDTO) {
        //todo: make transactions
        String id = keycloakService.createConsumer(consumerDTO);
        consumerService.createOne(id, consumerDTO);
        return id;
    }

    public ConsumerPoorView findOne(String id) {
        return consumerMapper.mapToConsumerPoorView(consumerService.findOne(id));
    }

    @Transactional
    //todo: не удлаять его отзывы.
    public void deleteOne(String id) {
        consumerService.deleteOne(id);
    }

    @Transactional
    public String updateOne(String id, ConsumerDTO consumerDTO) {
        return consumerService.updateOne(id, consumerDTO).getId().toString();
    }

    public Boolean existOne(String id) {
        return consumerService.existOne(id);
    }
}
