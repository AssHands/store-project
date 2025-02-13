package com.ak.store.consumer.facade;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.consumer.service.ConsumerService;
import com.ak.store.consumer.util.ConsumerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@RequiredArgsConstructor
public class ConsumerServiceFacade {
    private final ConsumerService consumerService;
    private final ConsumerMapper consumerMapper;

    public Long createOne(ConsumerDTO consumerDTO) {
        return consumerService.createOne(consumerDTO).getId();
    }

    public ConsumerDTO findOne(Long id) {
        return consumerMapper.mapToConsumerDTO(consumerService.findOne(id));
    }

    public void deleteOne(Long id) {
        consumerService.deleteOne(id);
    }

    public Long updateOne(Long id, ConsumerDTO consumerDTO) {
        return consumerService.updateOne(id, consumerDTO).getId();
    }
}
