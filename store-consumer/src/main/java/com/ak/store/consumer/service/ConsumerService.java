package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.repository.ConsumerRepo;
import com.ak.store.consumer.util.ConsumerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepo consumerRepo;
    private final ConsumerMapper consumerMapper;

    public Consumer createOne(ConsumerDTO consumerDTO) {
        Consumer consumer = consumerMapper.mapToConsumer(consumerDTO);
        return consumerRepo.save(consumer);
    }

    public Consumer findOne(Long id) {
        return consumerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("no consumers found"));
    }

    public void deleteOne(Long id) {
        consumerRepo.delete(findOne(id));
    }

    public Consumer updateOne(Long id, ConsumerDTO consumerDTO) {
        Consumer consumer = findOne(id);
        updateConsumer(consumer, consumerDTO);
        return consumerRepo.save(consumer);
    }

    private void updateConsumer(Consumer consumer, ConsumerDTO consumerDTO) {
        if(consumerDTO.getName() != null) {
            consumer.setName(consumerDTO.getName());
        }
        if(consumerDTO.getMail() != null) {
            consumer.setMail(consumerDTO.getMail());
        }
        if(consumerDTO.getPhone() != null) {
            consumer.setPhone(consumerDTO.getPhone());
        }
    }

    public Boolean existOne(Long id) {
        return consumerRepo.existsOneById(id);
    }
}
